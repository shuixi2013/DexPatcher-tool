package lanchon.dexpatcher;

import org.jf.dexlib2.iface.Annotatable;

public abstract class SimplePatcher<T extends Annotatable> extends AnnotationBasedPatcher<T> {

	protected SimplePatcher(Logger logger, String baseLogPrefix) {
		super(logger, baseLogPrefix);
	}

	protected SimplePatcher(AbstractPatcher<?> parent) {
		super(parent);
	}

	// Implementation

	@Override
	protected void onAdd(String patchId, T patch, PatcherAnnotation annotation) throws PatchException {
		T patched = onSimpleAdd(patch, annotation);
		addPatched(patchId, patch, patched);
	}

	@Override
	protected void onEdit(String patchId, T patch, PatcherAnnotation annotation) throws PatchException {
		String targetId = getTargetId(patchId, patch, annotation);
		extendLogPrefix(patchId, targetId, annotation);
		boolean renaming = !patchId.equals(targetId);
		T target = findTarget(targetId, !renaming);
		T patched = onSimpleEdit(patch, annotation, target, renaming);
		addPatched(patchId, patch, patched);
	}

	@Override
	protected void onReplace(String patchId, T patch, PatcherAnnotation annotation) throws PatchException {
		String targetId = getTargetId(patchId, patch, annotation);
		extendLogPrefix(patchId, targetId, annotation);
		T target = findTarget(targetId, false);
		T patched = onSimpleReplace(patch, annotation, target);
		addPatched(patchId, patch, patched);
	}

	@Override
	protected void onRemove(String patchId, T patch, PatcherAnnotation annotation) throws PatchException {
		String targetId = getTargetId(patchId, patch, annotation);
		extendLogPrefix(patchId, targetId, annotation);
		T target = findTarget(targetId, false);
		onSimpleRemove(patch, annotation, target);
	}

	// Handlers

	protected abstract String getTargetId(String patchId, T patch, PatcherAnnotation annotation);

	protected abstract T onSimpleAdd(T patch, PatcherAnnotation annotation);
	protected abstract T onSimpleEdit(T patch, PatcherAnnotation annotation, T target, boolean renaming);
	protected T onSimpleReplace(T patch, PatcherAnnotation annotation, T target) { return onSimpleAdd(patch, annotation); }
	protected void onSimpleRemove(T patch, PatcherAnnotation annotation, T target) {}

}
