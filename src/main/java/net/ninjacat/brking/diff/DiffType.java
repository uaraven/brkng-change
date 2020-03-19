package net.ninjacat.brking.diff;

public enum DiffType {
  ClassAdded(ChangeSeverity.SAFE),
  ClassRemoved(ChangeSeverity.BREAKING),
  ClassInterfaceAdded(ChangeSeverity.SAFE),
  ClassInterfaceRemoved(ChangeSeverity.BREAKING),
  ClassModifiersChanged(ChangeSeverity.BREAKING),
  ClassVisibilityChanged(ChangeSeverity.BREAKING),
  ClassVersionIncremented(ChangeSeverity.BREAKING),
  ClassVersionDecremented(ChangeSeverity.WARNING),
  ClassSuperClassChanged(ChangeSeverity.BREAKING),
  ClassSuperClassChangedToSubclass(ChangeSeverity.WARNING),
  ClassAnnotationsChanged(ChangeSeverity.WARNING),
  FieldModifiersChanged(ChangeSeverity.BREAKING),
  FieldRemoved(ChangeSeverity.BREAKING),
  FieldAdded(ChangeSeverity.SAFE),
  FieldTypeChanged(ChangeSeverity.BREAKING),
  FieldTypeChangedToSubclass(ChangeSeverity.WARNING),
  FieldVisibilityChanged(ChangeSeverity.BREAKING),
  FieldAnnotationsChanged(ChangeSeverity.WARNING),
  MethodModifiersChanged(ChangeSeverity.BREAKING),
  MethodRemoved(ChangeSeverity.BREAKING),
  MethodAdded(ChangeSeverity.SAFE),
  MethodReturnTypeChanged(ChangeSeverity.BREAKING),
  MethodReturnTypeChangedToSubclass(ChangeSeverity.WARNING),
  MethodVisibilityChanged(ChangeSeverity.BREAKING),
  MethodExceptionsChanged(ChangeSeverity.BREAKING),
  MethodAnnotationsChanged(ChangeSeverity.WARNING);

  private final ChangeSeverity severity;

  DiffType(final ChangeSeverity severity) {
    this.severity = severity;
  }

  public ChangeSeverity getSeverity() {
    return severity;
  }
}
