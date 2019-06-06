import React, {Component} from "react";
import "./SetsPane.scss";
import SetsApi from '../api/SetsApi';
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import {Button} from 'primereact/button';
import {InputText} from 'primereact/inputtext';

export default
class SetsTable extends Component{
  
  constructor(props) {
    super(props);
    this.state  = { data: this.props.data };
    this.api = new SetsApi();
  }
  
  actionTemplate(rowData, column) {
    return (<div>
              <Button type="button" icon="pi pi-trash" className="p-button-warning" 
                      title="Delete record"
                      onClick={() => this.onDelete(rowData)}/>
              <Button type="button" icon="pi pi-list" className="p-button-info" 
                      title="Show more information"
                      onClick={() => this.onInfo(rowData)}/>
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
    this.api.delete(props.id).then(result => {
      console.log("Delete", result);
      let updatedItems = [...this.state.data];
      this.setState({data: updatedItems.filter(rec => rec.id !=props.id)});
    });
  }
  
  onUpdate(props) {
    if (props.id) {
      this.api.update(props).then(result => {
        console.log("Update (modify)", props);
      });
    } else {
      this.api.create(props).then(result => {
        console.log("Update (insert)", props);
        let updatedItems = [...this.state.data];
        updatedItems.push(result);
        this.setState({data: updatedItems});
      });
    }
  }
  
  onAdd() {
    this.api.create().then(result => {
      console.log("Add", result);
      let updatedItems = [...this.state.data];
      updatedItems.push(result);
      this.setState({data: updatedItems});
    });
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
          <Column field="setName" header="Name" editor={props => this.nameEditor(props)} onEditorSubmit={props => this.onUpdate(props.rowData)} />
          <Column field="setSpec" header="Spec" editor={props => this.specEditor(props)} onEditorSubmit={props => this.onUpdate(props.rowData)} />
          <Column body={(rowData, column) => this.actionTemplate(rowData, column)} style={{textAlign:'center', width: '10em'}}/>
        </DataTable>
        <Button type="button" icon="pi pi-plus" className="p-button-info add" 
                title="Add new record"
                onClick={() => this.onAdd()}/>
      </div>
    );
  }
}
