package com.qxdzbc.p6.di.state.app_state;

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
public final class CellFormatTableModule_Companion_FloatFormatTableFactory implements Factory<FlyweightTable<Float>> {
  @Override
  public FlyweightTable<Float> get() {
    return FloatFormatTable();
  }

  public static CellFormatTableModule_Companion_FloatFormatTableFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FlyweightTable<Float> FloatFormatTable() {
    return Preconditions.checkNotNullFromProvides(CellFormatTableModule.Companion.FloatFormatTable());
  }

  private static final class InstanceHolder {
    private static final CellFormatTableModule_Companion_FloatFormatTableFactory INSTANCE = new CellFormatTableModule_Companion_FloatFormatTableFactory();
  }
}
