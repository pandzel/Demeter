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
//    this.api.delete(props.id).then(result => {
//      this.props.onPageChange(this.state.data.page);
//    });
  }
  
  onUpdate = (props) => {
//    const rowData = props.rowData;
//    if (rowData.id) {
//      this.api.update(rowData).then(result => {
//      });
//    } else {
//      this.api.create(rowData).then(result => {
//        let updatedItems = [...this.state.data.data];
//        updatedItems.push(result);
//        var data = {...this.state.data};
//        data.data = updatedItems;
//        this.setState({data: data});
//      });
//    }
  }
  
  onAdd = () => {
//    this.api.create().then(result => {
//      let updatedItems = [...this.state.data.data];
//      updatedItems.push(result);
//      var data = {...this.state.data};
//      data.data = updatedItems;
//      data.total++;
//      this.setState({data: data});
//    });
  }
  
  actionTemplate = (rowData, column) => {
    return (<div>
              <Button type="button" icon="pi pi-trash" className="p-button-warning action-button delete-button" 
                      title="Delete record"
                      onClick={() => this.onDelete(rowData)}/>
              <Button type="button" icon="pi pi-list" className="p-button-info action-button info-button" 
                      title="Show records within the set"
                      onClick={() => this.loadRecords(rowData)}/>
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
      let updatedItems = [...this.state.data.data];
      updatedItems[props.rowIndex][props.field] = value;
      this.setState({data: {...this.state.data, data: updatedItems}});
  }
  
  render(){
    return(
      <div className="SetsTable">
        {this.state.data && <Paginator first={this.state.data.page * this.state.data.pageSize} rows={this.state.data.pageSize} totalRecords={this.state.data.total} onPageChange={(e) => this.load(e.page)}></Paginator>}
        {this.state.data && <DataTable value={this.state.data.data}>
          <Column field="setName" header="Name" editor={this.nameEditor} onEditorSubmit={this.onUpdate} />
          <Column field="setSpec" header="Spec" editor={this.specEditor} onEditorSubmit={this.onUpdate} />
          <Column body={(rowData, column) => this.actionTemplate(rowData, column)} className="action-buttons"/>
        </DataTable>}
        <Button type="button" icon="pi pi-plus" className="p-button-info add" 
                title="Add new record"
                onClick={this.onAdd}/>
      </div>
    );
  }
}
