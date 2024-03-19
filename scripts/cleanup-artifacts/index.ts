import { Octokit } from '@octokit/rest';
const octokit = new Octokit({ auth: process.env.GITHUB_TOKEN });

async function cleanupArtifacts() {
	const owner = 'spartacus04';
	const repo = 'jext-reborn';
	const workflowId = 'build-tauri.yml';

	// List all workflow runs
	const { data: runs } = await octokit.actions.listWorkflowRuns({
		owner,
		repo,
		workflow_id: workflowId
	});

	// Find the latest successful run
	const latestSuccessfulRun = runs.workflow_runs.find(
		(run) => run.status === 'completed' && run.conclusion === 'success'
	)!;

	// Delete artifacts for all runs except the latest successful one
	for (const run of runs.workflow_runs) {
		if (run.id !== latestSuccessfulRun.id) {
			const { data: artifacts } = await octokit.actions.listWorkflowRunArtifacts({
				owner,
				repo,
				run_id: run.id
			});

			for (const artifact of artifacts.artifacts) {
				await octokit.actions.deleteArtifact({
					owner,
					repo,
					artifact_id: artifact.id
				});
			}
		}
	}
}

cleanupArtifacts().catch(console.error);
