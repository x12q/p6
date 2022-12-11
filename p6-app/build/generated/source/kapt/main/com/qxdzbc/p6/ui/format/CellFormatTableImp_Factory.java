package com.qxdzbc.p6.ui.format;

import androidx.compose.ui.graphics.Color;
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment;
import com.qxdzbc.p6.ui.format.attr.BoolAttr;
import com.qxdzbc.p6.ui.format.flyweight.FlyweightTable;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class CellFormatTableImp_Factory implements Factory<CellFormatTableImp> {
  private final Provider<FlyweightTable<Float>> floatTableProvider;

  private final Provider<FlyweightTable<Color>> colorTableProvider;

  private final Provider<FlyweightTable<BoolAttr>> boolTableProvider;

  private final Provider<FlyweightTable<TextHorizontalAlignment>> horizontalAlignmentTableProvider;

  public CellFormatTableImp_Factory(Provider<FlyweightTable<Float>> floatTableProvider,
      Provider<FlyweightTable<Color>> colorTableProvider,
      Provider<FlyweightTable<BoolAttr>> boolTableProvider,
      Provider<FlyweightTable<TextHorizontalAlignment>> horizontalAlignmentTableProvider) {
    this.floatTableProvider = floatTableProvider;
    this.colorTableProvider = colorTableProvider;
    this.boolTableProvider = boolTableProvider;
    this.horizontalAlignmentTableProvider = horizontalAlignmentTableProvider;
  }

  @Override
  public CellFormatTableImp get() {
    return newInstance(floatTableProvider.get(), colorTableProvider.get(), boolTableProvider.get(), horizontalAlignmentTableProvider.get());
  }

  public static CellFormatTableImp_Factory create(Provider<FlyweightTable<Float>> floatTableProvider,
      Provider<FlyweightTable<Color>> colorTableProvider,
      Provider<FlyweightTable<BoolAttr>> boolTableProvider,
      Provider<FlyweightTable<TextHorizontalAlignment>> horizontalAlignmentTableProvider) {
    return new CellFormatTableImp_Factory(floatTableProvider, colorTableProvider, boolTableProvider, horizontalAlignmentTableProvider);
  }

  public static CellFormatTableImp newInstance(FlyweightTable<Float> floatTable,
                                               FlyweightTable<Color> colorTable, FlyweightTable<BoolAttr> boolTable,
                                               FlyweightTable<TextHorizontalAlignment> horizontalAlignmentTable) {
    return new CellFormatTableImp(floatTable, colorTable, boolTable, horizontalAlignmentTable);
  }
}
