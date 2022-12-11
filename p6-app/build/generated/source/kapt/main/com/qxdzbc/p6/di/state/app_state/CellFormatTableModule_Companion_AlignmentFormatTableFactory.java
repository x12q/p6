package com.qxdzbc.p6.di.state.app_state;

import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment;
import com.qxdzbc.p6.ui.format.flyweight.FlyweightTable;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class CellFormatTableModule_Companion_AlignmentFormatTableFactory implements Factory<FlyweightTable<TextHorizontalAlignment>> {
  @Override
  public FlyweightTable<TextHorizontalAlignment> get() {
    return AlignmentFormatTable();
  }

  public static CellFormatTableModule_Companion_AlignmentFormatTableFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FlyweightTable<TextHorizontalAlignment> AlignmentFormatTable() {
    return Preconditions.checkNotNullFromProvides(CellFormatTableModule.Companion.AlignmentFormatTable());
  }

  private static final class InstanceHolder {
    private static final CellFormatTableModule_Companion_AlignmentFormatTableFactory INSTANCE = new CellFormatTableModule_Companion_AlignmentFormatTableFactory();
  }
}
