import React, {Component} from "react";
import "./SetsPane.scss";
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import {Button} from 'primereact/button';
import {InputText} from 'primereact/inputtext';

export default
class SetsTable extends Component{
  constructor(props) {
    super(props);
    this.state  = { data: this.props.data };
  }
  
  actionTemplate(rowData, column) {
    return (<div>
              <Button type="button" icon="pi pi-trash" className="p-button-warning" onClick={() => this.onDelete(rowData)}></Button>
              <Button type="button" icon="pi pi-folder-open" className="p-button-info" onClick={() => this.onInfo(rowData)}></Button>
            </div>);
  }
    
  onEditorValueChange(props, value) {
      let updatedItems = [...this.state.data];
      updatedItems[props.rowIndex][props.field] = value;
      this.setState({data: updatedItems});
  }
  
  onInfo(props) {
    // display info
    console.log("More Info", props);
  }
  
  onDelete(props) {
    // save state
    console.log("Delete", props);
  }
  
  onUpdate(props) {
    // save state
    console.log("Update", props);
  }

  nameEditor(props) {
      return <InputText type="text" value={this.state.data[props.rowIndex]['setName']} 
              onChange={(e) => this.onEditorValueChange(props, e.target.value)} />;
  }    

  specEditor(props) {
      return <InputText type="text" value={this.state.data[props.rowIndex]['setSpec']} 
              onChange={(e) => this.onEditorValueChange(props, e.target.value)} />;
  }    
  
  render(){
    return(
      <div className="SetsTable">
        <DataTable value={this.state.data}>
          <Column field="setName" header="Name" editor={props => this.nameEditor(props)} onEditorSubmit={props => this.onUpdate(props)} />
          <Column field="setSpec" header="Spec" editor={props => this.specEditor(props)}/>
          <Column body={(rowData, column) => this.actionTemplate(rowData, column)} style={{textAlign:'center', width: '10em'}}/>
        </DataTable>
      </div>
    );
  }
}
