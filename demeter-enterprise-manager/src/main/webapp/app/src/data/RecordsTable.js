import React, {Component} from "react";
import "./DataPane.scss";
import RecordsApi from '../api/RecordsApi';
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import {Button} from 'primereact/button';
import {InputText} from 'primereact/inputtext';
import EditorPane from './EditorPane';

export default
class RecordsTable extends Component {
  
  state  = { 
    data: this.props.data,
    current: null
  };
  
  api = new RecordsApi();
  
  actionTemplate = (rowData, column) => {
    return (<div>
              <Button type="button" icon="pi pi-trash" className="p-button-warning action-button delete-button" 
                      title="Delete record"
                      onClick={() => this.onDelete(rowData)}/>
              <Button type="button" icon="pi pi-pencil" className="p-button-edit action-button edit-button" 
                      title="Edit record"
                      onClick={() => this.onEdit(rowData)}/>
              <Button type="button" icon="pi pi-list" className="p-button-info action-button info-button" 
                      title="Show more information"
                      onClick={() => this.onInfo(rowData)}/>
            </div>);
  }
  
  onInfo = (props) => {
    console.log("More info", props);
  }
  
  onEdit = (props) => {
    console.log("Editing data", props);
    this.setState({current: props});
  }
  
  onDelete = (props) => {
    console.log("Deleting data", props);
  }
  
  onAdd = () => {
    console.log("Adding new record");
  }
  
  render(){
    
    let dataTable = 
      <div>
        <DataTable value={this.state.data}>
          <Column field="title" header="Title"/>
          <Column field="description" header="Description"/>
          <Column body={(rowData, column) => this.actionTemplate(rowData, column)} className="action-buttons"/>
        </DataTable>
        <Button type="button" icon="pi pi-plus" className="p-button-info add" 
                title="Add new record"
                onClick={this.onAdd}/>
      </div>;

    let editorPane = <EditorPane record={this.state.current}/>;
    
    return(
      <div className="RecordsTable">
        {this.state.current? editorPane: dataTable}
      </div>
    );
  }
}