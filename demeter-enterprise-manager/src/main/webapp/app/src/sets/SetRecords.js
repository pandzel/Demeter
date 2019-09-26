import React, { Component} from "react";
import "./Sets.scss";
import {Button} from 'primereact/button';
import {Paginator} from 'primereact/paginator';
import SetsApi from '../api/SetsApi';
import SetRecord from './SetRecord';

export default
class SetRecords extends Component{
  state = {
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
    this.api.listRecords(this.props.currentSet.id, page).then(records => {
      console.log(records);
      this.setState({data: records});
    }).catch(this.props.onError);
  }
  
  onRemove = (key) => {
    console.log("Removinf", key);
    this.api.delCollection(this.props.currentSet.id, key).then(result => {
      this.load(this.state.data.page);
    }).catch(this.onError);
  }
  
  render(){
    const COLS = this.props.cols || 2;
    
    let rowsOfData = this.state.data.data.reduce((acc,record,idx) => {
      let pos = Math.floor(idx/COLS);
      while (acc.length<=pos)
        acc.push([]);
      acc[pos].push(record);
      return acc;
    }, []);
    
    let rowsOfRecords = rowsOfData.map((row, ridx) => 
      <div key={ridx} style={{display: "table-row"}}>
        {row.map(record => (
          <div key={record.key} className="recordCell" style={{display: "table-cell"}}>
            <SetRecord key={record.key} record={record} onRemove={this.onRemove}/>
          </div>
        ))}
      </div>
    );
    
    return(
      <div className="SetRecords">
        <div className="Title">{`Records included in "${this.props.currentSet.setName}"`}</div>
        <Paginator first={this.state.data.page * this.state.data.pageSize} rows={this.state.data.pageSize} totalRecords={this.state.data.total} onPageChange={(e) => this.load(e.page)}></Paginator>
        {rowsOfRecords}
        <Button label="Back" onClick={this.props.onExit}/>
      </div>
    );
  }
}
