package com.qxdzbc.p6.ui.format.action;

import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.State;
import com.qxdzbc.p6.app.action.cell.update_cell_format.UpdateCellFormatActionImp;
import com.qxdzbc.p6.ui.app.state.StateContainer;
import com.qxdzbc.p6.ui.format.CellFormatTable;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("com.qxdzbc.p6.di.P6Singleton")
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
public final class UpdateCellFormatActionImp_Factory implements Factory<UpdateCellFormatActionImp> {
  private final Provider<MutableState<CellFormatTable>> cellFormatTableMsProvider;

  private final Provider<State<StateContainer>> stateContainerStProvider;

  public UpdateCellFormatActionImp_Factory(
      Provider<MutableState<CellFormatTable>> cellFormatTableMsProvider,
      Provider<State<StateContainer>> stateContainerStProvider) {
    this.cellFormatTableMsProvider = cellFormatTableMsProvider;
    this.stateContainerStProvider = stateContainerStProvider;
  }

  @Override
  public UpdateCellFormatActionImp get() {
    return newInstance(cellFormatTableMsProvider.get(), stateContainerStProvider.get());
  }

  public static UpdateCellFormatActionImp_Factory create(
      Provider<MutableState<CellFormatTable>> cellFormatTableMsProvider,
      Provider<State<StateContainer>> stateContainerStProvider) {
    return new UpdateCellFormatActionImp_Factory(cellFormatTableMsProvider, stateContainerStProvider);
  }

  public static UpdateCellFormatActionImp newInstance(
      MutableState<CellFormatTable> cellFormatTableMs, State<StateContainer> stateContainerSt) {
    return new UpdateCellFormatActionImp(cellFormatTableMs, stateContainerSt);
  }
}
