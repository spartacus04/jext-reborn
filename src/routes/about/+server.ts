import fs from 'node:fs';
import child_process from 'node:child_process';
import toml from 'toml';
import { json } from '@sveltejs/kit';

export const prerender = true;

const getJsDependencies = () => {
    const { dependencies, devDependencies } = JSON.parse(fs.readFileSync('package.json', 'utf8'))

    const jsDependenciesTitles = Object.keys({ ...dependencies, ...devDependencies })

    return jsDependenciesTitles.map((name: string) => {
        const { version } = JSON.parse(fs.readFileSync(`node_modules/${name}/package.json`, 'utf8'))
        
        const licenseFiles = fs.readdirSync(`node_modules/${name}`).filter((file: string) => file.match(/license/i))

        const license = licenseFiles.map((file: string) => fs.readFileSync(`node_modules/${name}/${file}`, 'utf8')).join('\n---\n')

        return {
            name,
            version,
            license: license.length > 0 ? license : 'Unknown',
        }
    })
}

const getRustDependencies = () => {
    const data = toml.parse(fs.readFileSync('src-tauri/Cargo.toml', 'utf8'))

    const dependencies = Object.assign({ ...data.dependencies, ...data['build-dependencies'] })

    const userPath = process.env.HOME || process.env.USERPROFILE

    const cargoRegistryPath = `${userPath}/.cargo/registry/src`

    if (!fs.existsSync(cargoRegistryPath)) {
        return []
    }

    const registry = fs.readdirSync(cargoRegistryPath)[0]

    const rustDependencies = []

    for (const key in dependencies) {
        const version = typeof dependencies[key] === 'string' ? dependencies[key] : dependencies[key].version

        const dependencyPath = fs.readdirSync(`${cargoRegistryPath}/${registry}`).filter((file: string) => file.includes(`${key}-${version}`))[0]!

        const licenseFiles = fs.readdirSync(`${cargoRegistryPath}/${registry}/${dependencyPath}`).filter((file: string) => file.match(/license/i))

        const license = licenseFiles.map((file: string) => fs.readFileSync(`${cargoRegistryPath}/${registry}/${dependencyPath}/${file}`, 'utf8')).join('\n---\n')

        rustDependencies.push({
            name: key,
            version,
            license: license.length > 0 ? license : 'Unknown',
        })
    }

    return rustDependencies
}

const executeCommand = async (command: string) : Promise<string> => {
    return new Promise((resolve, reject) => {
        child_process.exec(command, (error, stdout) => {
            if (error) {
                reject(error);
            }
            resolve(stdout.trim());
        });
    });
}

export const GET = async () => {
    const webuiVersion = JSON.parse(fs.readFileSync('package.json', 'utf8')).version
    const desktopVersion = JSON.parse(fs.readFileSync('src-tauri/tauri.conf.json', 'utf8')).version
    const commit = await executeCommand('git rev-parse HEAD');
    const date = await executeCommand('git --no-pager log -1 --pretty="format:%cd" --date="format:%Y-%m-%d %H:%M:%S"');

    return json({
        webuiVersion,
        desktopVersion,
        commit,
        date: new Date(date).toLocaleString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric', hour12: true }),
        jsLicenses: getJsDependencies(),
        rustLicenses: getRustDependencies(),
    })
}