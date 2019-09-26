import React, { Component} from "react";
import "./Data.scss";
import DataApi from '../api/RecordsApi';

import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import {Button} from 'primereact/button';
import {InputText} from 'primereact/inputtext';
import {Paginator} from 'primereact/paginator';

export default
class DataTableComponent extends Component{
  state = {
    data: {
      page: 0,
      pageSize: 0,
      total: 0,
      data: []
    }
  };
  
  api = new DataApi();
  
  componentDidMount() {
    this.load(this.props.page);
  }
  
  load = (page) => {
    this.api.list(page).then(data => {
      this.setState({data: data});
    }).catch(this.props.onError);
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
    // this.props.onInfo(props);
  }
  
  onEdit = (props) => {
    this.props.onEdit(props, this.state.data.page);
  }
  
  onDelete = (props) => {
    // this.props.onDelete(props);
  }
  
  onAdd = (props) => {
    
  }
  
  onPageChange = (page) => {
    
  }
  
  render(){
    return(
      <div className="DataTable">
        <Paginator first={this.state.data.page * this.state.data.pageSize} rows={this.state.data.pageSize} totalRecords={this.state.data.total} onPageChange={(e) => this.onPageChange(e.page)}></Paginator>
        <DataTable value={this.state.data.data}>
          <Column field="title" header="Title"/>
          <Column field="description" header="Description"/>
          <Column body={(rowData, column) => this.actionTemplate(rowData, column)} className="action-buttons"/>
        </DataTable>
        <Button type="button" icon="pi pi-plus" className="p-button-info add" 
                title="Add new record"
                onClick={this.onAdd}/>
      </div>
    );
  }
}
