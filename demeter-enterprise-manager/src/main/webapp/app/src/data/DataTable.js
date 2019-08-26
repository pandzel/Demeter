import React, {Component} from "react";
import "./DataPane.scss";
import RecordsApi from '../api/RecordsApi';
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import {Button} from 'primereact/button';
import {InputText} from 'primereact/inputtext';

export default
class SetsTable extends Component{
  
  state  = { 
    data: this.props.data 
  };
  
  api = new RecordsApi();
  
  render(){
    return(
      <div className="DataTable">
        <DataTable value={this.state.data}>
          <Column field="title" header="Title"/>
          <Column field="description" header="Description"/>
        </DataTable>
      </div>
    );
  }
}
