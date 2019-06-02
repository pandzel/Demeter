import React, {Component} from "react";
import "./SetsPane.scss";
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';

export default
class SetsTable extends Component{
  constructor(props) {
    super(props);
  }
  
  render(){
    const items = this.props.data && this.props.data.map(item => 
    <li key={item.id}><span>{item.setName}</span> [<span>{item.setSpec}</span>]</li>
    );
    return(
      <div className="SetsTable">
        <DataTable value={this.props.data}>
          <Column field="setSpec" header="Spec"/>
          <Column field="setName" header="Name"/>
        </DataTable>
      </div>
    );
  }
}
