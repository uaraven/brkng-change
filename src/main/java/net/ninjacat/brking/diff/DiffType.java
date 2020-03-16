package net.ninjacat.brking.diff;

public enum DiffType {
    ClassInterfaceAdded(ChangeSeverity.SAFE),
    ClassInterfaceRemoved(ChangeSeverity.BREAKING),
    ClassModifiersChanged(ChangeSeverity.BREAKING),
    ClassVisibilityChanged(ChangeSeverity.BREAKING),
    ClassVersionIncremented(ChangeSeverity.BREAKING),
    ClassVersionDecremented(ChangeSeverity.WARNING),
    ClassSuperClassChanged(ChangeSeverity.BREAKING),
    FieldModifiersChanged(ChangeSeverity.BREAKING),
    FieldRemoved(ChangeSeverity.BREAKING),
    FieldAdded(ChangeSeverity.SAFE),
    FieldTypeChanged(ChangeSeverity.BREAKING),
    FieldVisibilityChanged(ChangeSeverity.BREAKING),
    MethodModifiersChanged(ChangeSeverity.BREAKING),
    MethodRemoved(ChangeSeverity.BREAKING),
    MethodAdded(ChangeSeverity.SAFE),
    MethodReturnTypeChanged(ChangeSeverity.BREAKING),
    MethodVisibilityChanged(ChangeSeverity.BREAKING),
    MethodExceptionsChanged(ChangeSeverity.BREAKING);

    private final ChangeSeverity severity;

    DiffType(final ChangeSeverity severity) {
        this.severity = severity;
    }

    public ChangeSeverity getSeverity() {
        return severity;
    }
}
