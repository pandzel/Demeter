import React, { Component } from "react";
import "./SetsPane.scss";
import {Button} from 'primereact/button';

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

  render(){
    return(
      <div className="RecordsList">
        <div className="Records">
          {this.props.records.data.map(record => <Record key={record.key} record={record} onRemove={this.onRemove}/>)}
        </div>
        <Button label="Cancel" onClick={this.props.onExit}/>
      </div>
    );
  }
}