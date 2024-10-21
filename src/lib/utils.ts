import { get } from "svelte/store";
import { ConfirmModal } from "./components/modals";
import { baseElement } from "./state";

export const cConfirm = async (options: {
    text: string,
    confirmText: string,
    cancelText: string|undefined,
    discardText: string,
}) => {
    return await new Promise<'discard' | 'confirm' | 'cancel'>((resolve) => {
        const confirmModal = new ConfirmModal({
            target: get(baseElement)!,
            props: {
                ...options,
                onFinish: (result) => {
                    confirmModal.$destroy();
                    resolve(result);
                }
            }
        });

        confirmModal.openModal();
    });
}