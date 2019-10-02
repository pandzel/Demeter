import React, { Component} from "react";
import "./Data.scss";
import DataApi from '../api/RecordsApi';

import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import {Button} from 'primereact/button';
import {InputText} from 'primereact/inputtext';
import {Paginator} from 'primereact/paginator';
import {Dialog} from 'primereact/dialog';

export default
class DataTableComponent extends Component{
  state = {
    data: {
      page: 0,
      pageSize: 0,
      total: 0,
      data: []
    },
    dialogVisible: false
  };
  
  rowDataForDeletion = null;
  
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
                      onClick={() => this.onAskDelete(rowData)}/>
              <Button type="button" icon="pi pi-pencil" className="p-button-edit action-button edit-button" 
                      title="Edit record"
                      onClick={() => this.onEdit(rowData)}/>
              <Button type="button" icon="pi pi-list" className="p-button-info action-button info-button" 
                      title="Show more information"
                      onClick={() => this.onInfo(rowData)}/>
            </div>);
  }
  
  onInfo = (props) => {
    this.props.onShowSets(props);
  }
  
  onEdit = (props) => {
    this.props.onEdit(props, this.state.data.page);
  }
  
  onAskDelete = (props) => {
    this.rowDataForDeletion = props;
    this.setState({dialogVisible: true});
  }
  
  onCloseDelete = () => {
    this.rowDataForDeletion = null;
    this.setState({dialogVisible: false});
  }
  
  onDelete = (props) => {
    this.onCloseDelete();
    this.api.delete(props.id)
            .then(result => this.load(this.state.data.page))
            .catch(this.onError);
  }
  
  onAdd = (props) => {
    let record = {
      title: "",
      creator: "",
      subject: "",
      description: "",
      publisher: "",
      contributor: "",
      date: "",
      identifier: "",
      format: "",
      source: "",
      language: "",
      relation: "",
      coverage: "",
      rights: ""
    };
    this.props.onEdit(record, this.state.data.page);
  }
  
  render(){
    const footer = (
        <div>
            <Button label="Yes" icon="pi pi-check" onClick={() => this.onDelete(this.rowDataForDeletion)} />
            <Button label="No" icon="pi pi-times" onClick={this.onCloseDelete} />
        </div>
    );    
    
    return(
      <div className="DataTable">
        <Paginator first={this.state.data.page * this.state.data.pageSize} rows={this.state.data.pageSize} totalRecords={this.state.data.total} onPageChange={(e) => this.load(e.page)}></Paginator>
        <DataTable value={this.state.data.data}>
          <Column field="title" header="Title"/>
          <Column field="description" header="Description"/>
          <Column body={(rowData, column) => this.actionTemplate(rowData, column)} className="action-buttons"/>
        </DataTable>
        <Button type="button" icon="pi pi-plus" className="p-button-info add" 
                title="Add new record"
                onClick={this.onAdd}/>
        <Dialog header="Action" visible={this.state.dialogVisible} style={{width: '20vw'}} modal={true} footer={footer} onHide={() => this.onCloseDelete()}>
                Confirm deletion of '{this.rowDataForDeletion? this.rowDataForDeletion.title: ""}' record.
        </Dialog>                        
      </div>
    );
  }
}
