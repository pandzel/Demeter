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
    this.setNameEditor = this.setNameEditor.bind(this);
    this.setSpecEditor = this.setSpecEditor.bind(this);
  }
  
  actionTemplate(rowData, column) {
    return (<div>
              <Button type="button" icon="pi pi-trash" className="p-button-warning"></Button>
            </div>);
  }
    
  onEditorValueChange(props, value) {
      let updatedItems = [...this.state.data];
      updatedItems[props.rowIndex][props.field] = value;
      this.setState({data: updatedItems});
  }

  setNameEditor(props) {
      return <InputText type="text" value={this.state.data[props.rowIndex]['setName']} onChange={(e) => this.onEditorValueChange(props, e.target.value)} />;
  }    

  setSpecEditor(props) {
      return <InputText type="text" value={this.state.data[props.rowIndex]['setSpec']} onChange={(e) => this.onEditorValueChange(props, e.target.value)} />;
  }    
  
  render(){
    return(
      <div className="SetsTable">
        <DataTable value={this.state.data}>
          <Column field="setName" header="Name" editor={this.setNameEditor}/>
          <Column field="setSpec" header="Spec" editor={this.setSpecEditor}/>
          <Column body={this.actionTemplate} style={{textAlign:'center', width: '6em'}}/>
        </DataTable>
      </div>
    );
  }
}
