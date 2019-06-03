import React, {Component} from "react";
import "./SetsPane.scss";
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import {Button} from 'primereact/button';

export default
class SetsTable extends Component{
  constructor(props) {
    super(props);
    this.state  = { data: this.props.data };
  }
  
  actionTemplate(rowData, column) {
    return (<div>
              <Button type="button" icon="pi pi-trash" className="p-button-warning"></Button>
            </div>);
  }
    
  render(){
    return(
      <div className="SetsTable">
        <DataTable value={this.state.data}>
          <Column field="setName" header="Name"/>
          <Column field="setSpec" header="Spec"/>
          <Column body={this.actionTemplate} style={{textAlign:'center', width: '6em'}}/>
        </DataTable>
      </div>
    );
  }
}
