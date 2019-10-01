import React, {Component} from "react";
import "./Sets.scss";

import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import {Button} from 'primereact/button';
import {InputText} from 'primereact/inputtext';
import {Paginator} from 'primereact/paginator';

import SetsApi from '../api/SetsApi';

export default
class SetsTable extends Component{
  
  state  = { 
    data: {
      page: 0,
      pageSize: 0,
      total: 0,
      data: []
    }
  };
  
  api = new SetsApi();
  
  componentDidMount() {
    this.load();
  }
  
  load = (page) => {
    this.api.list(page).then(result => {
      this.setState({ data: result });
    }).catch(this.props.onError);
  }
  
  onDelete = (props) => {
    this.api.delete(props.id).then(result => {
      this.load(this.state.data.page);
    }).catch(this.props.onError);
  }
  
  onUpdate = (props) => {
    this.api.update(props.rowData).catch(this.props.onError);
  }
  
  onAdd = () => {
    this.api.create().then(result => {
      let items = [...this.state.data.data];
      items.push(result);
      this.setState({data: {data: items}, total: this.state.data.total+1});
    }).catch(this.props.onError);
  }
  
  actionTemplate = (rowData, column) => {
    return (<div>
              <Button type="button" icon="pi pi-trash" className="p-button-warning action-button delete-button" 
                      title="Delete record"
                      onClick={() => this.onDelete(rowData)}/>
              <Button type="button" icon="pi pi-list" className="p-button-info action-button info-button" 
                      title="Show records within the set"
                      onClick={() => this.props.onShowRecords(rowData)}/>
            </div>);
  }

  nameEditor = (props) => {
      return <InputText type="text" value={this.state.data.data[props.rowIndex]['setName']} 
              onChange={(e) => this.onEditorValueChange(props, e.target.value)} />;
  }    

  specEditor = (props) => {
      return <InputText type="text" value={this.state.data.data[props.rowIndex]['setSpec']} 
              onChange={(e) => this.onEditorValueChange(props, e.target.value)} />;
  }    
    
  onEditorValueChange = (props, value) => {
      let items = [...this.state.data.data];
      items[props.rowIndex][props.field] = value;
      this.setState({data: {...this.state.data, data: items}});
  }
  
  render(){
    return(
      <div className="SetsTable">
        <Paginator first={this.state.data.page * this.state.data.pageSize} rows={this.state.data.pageSize} totalRecords={this.state.data.total} onPageChange={(e) => this.load(e.page)}></Paginator>
        <DataTable value={this.state.data.data}>
          <Column field="setName" header="Name" editor={this.nameEditor} onEditorSubmit={this.onUpdate} />
          <Column field="setSpec" header="Spec" editor={this.specEditor} onEditorSubmit={this.onUpdate} />
          <Column body={this.actionTemplate} className="action-buttons"/>
        </DataTable>
        <Button type="button" icon="pi pi-plus" className="p-button-info add" 
                title="Add new set"
                onClick={this.onAdd}/>
      </div>
    );
  }
}
