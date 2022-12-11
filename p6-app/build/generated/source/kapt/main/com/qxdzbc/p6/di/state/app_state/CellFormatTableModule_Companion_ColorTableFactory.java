package com.qxdzbc.p6.di.state.app_state;

import androidx.compose.ui.graphics.Color;
import com.qxdzbc.common.flyweight.FlyweightTable;
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
public final class CellFormatTableModule_Companion_ColorTableFactory implements Factory<FlyweightTable<Color>> {
  @Override
  public FlyweightTable<Color> get() {
    return colorTable();
  }

  public static CellFormatTableModule_Companion_ColorTableFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FlyweightTable<Color> colorTable() {
    return Preconditions.checkNotNullFromProvides(CellFormatTableModule.Companion.colorTable());
  }

  private static final class InstanceHolder {
    private static final CellFormatTableModule_Companion_ColorTableFactory INSTANCE = new CellFormatTableModule_Companion_ColorTableFactory();
  }
}
