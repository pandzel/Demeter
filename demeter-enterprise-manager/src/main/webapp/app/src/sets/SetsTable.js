import React, {Component} from "react";
import "./SetsPane.scss";
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';

export default
class SetsTable extends Component{
  constructor(props) {
    super(props);
    this.state  = { data: this.props.data };
  }
  
  render(){
    return(
      <div className="SetsTable">
        <DataTable value={this.state.data}>
          <Column field="setName" header="Name"/>
          <Column field="setSpec" header="Spec"/>
        </DataTable>
      </div>
    );
  }
}
