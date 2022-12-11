package com.qxdzbc.p6.di.state.app_state;

import com.qxdzbc.common.flyweight.FlyweightTable;
import com.qxdzbc.p6.ui.format.attr.BoolAttr;
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
public final class CellFormatTableModule_Companion_BoolFormatTableFactory implements Factory<FlyweightTable<BoolAttr>> {
  @Override
  public FlyweightTable<BoolAttr> get() {
    return BoolFormatTable();
  }

  public static CellFormatTableModule_Companion_BoolFormatTableFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FlyweightTable<BoolAttr> BoolFormatTable() {
    return Preconditions.checkNotNullFromProvides(CellFormatTableModule.Companion.BoolFormatTable());
  }

  private static final class InstanceHolder {
    private static final CellFormatTableModule_Companion_BoolFormatTableFactory INSTANCE = new CellFormatTableModule_Companion_BoolFormatTableFactory();
  }
}
