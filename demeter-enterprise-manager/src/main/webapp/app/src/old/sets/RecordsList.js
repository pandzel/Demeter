import React, { Component } from "react";
import "./SetsPane.scss";
import {Button} from 'primereact/button';
import {Paginator} from 'primereact/paginator';

function Record(props) {
  return <div className="Record">
            <span>
              <Button type="button" icon="pi pi-times-circle"
                      title="Remove"
                      onClick={() => props.onRemove(props.record.key)}/>
            </span>
            <span>{props.record.value}</span>
         </div>;
};

export default
class RecordsList extends Component {
  state = {};
  
  componentDidMount() {
    console.log(this.props.records);
  }
  
  onRemove = (key) => {
    this.props.onRemove(key);
  }
  
  onPageChange = (page) => {
    this.props.loadRecords(page);
  }

  render(){
    const COLS = this.props.cols || 2;
    
    let reduced = this.props.records.data.reduce((acc,record,idx) => {
      let pos = Math.floor(idx/COLS);
      while (acc.length<=pos)
        acc.push([]);
      acc[pos].push(record);
      return acc;
    }, []);
    
    let rows = reduced.map((row, ridx) => <div key={ridx} style={{display: "table-row"}}>{row.map(record => (<div key={record.key} className="recordCell" style={{display: "table-cell"}}><Record key={record.key} record={record} onRemove={this.onRemove}/></div>))}</div>);
    return(
      <div className="RecordsList">
        <Paginator first={this.props.records.page * this.props.records.pageSize} rows={this.props.records.pageSize} totalRecords={this.props.records.total} onPageChange={(e) => this.onPageChange(e.page)}></Paginator>
        <div className="Records">
          {rows}
        </div>
        <Button label="Back" onClick={this.props.onExit}/>
      </div>
    );
  }
}