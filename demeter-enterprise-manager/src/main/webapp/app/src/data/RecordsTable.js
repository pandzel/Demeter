import React, {Component} from "react";
import "./DataPane.scss";

import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import {Button} from 'primereact/button';
import {InputText} from 'primereact/inputtext';

export default
class RecordsPane extends Component {
  
  state = {
    records: this.props.records
  }
  
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
    this.props.onInfo(props);
  }
  
  onEdit = (props) => {
    this.props.onEdit(props);
  }
  
  onDelete = (props) => {
    this.props.onDelete(props);
  }
  
  render() {
    return (
      <div>
        <DataTable value={this.state.records}>
          <Column field="title" header="Title"/>
          <Column field="description" header="Description"/>
          <Column body={(rowData, column) => this.actionTemplate(rowData, column)} className="action-buttons"/>
        </DataTable>
      </div>
    );
  }
}