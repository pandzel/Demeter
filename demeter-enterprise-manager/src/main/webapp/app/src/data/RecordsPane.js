import React, {Component} from "react";
import "./DataPane.scss";

import {Button} from 'primereact/button';

import RecordsApi from '../api/RecordsApi';
import EditorPane from './EditorPane';
import RecordsTable from './RecordsTable';

function assureRecord(record) {
  record = record || {};
  
  record.title = record.title || "";
  record.creator = record.creator || "";
  record.subject = record.subject || "";
  record.description = record.description || "";
  record.publisher = record.publisher || "";
  record.contributor = record.contributor || "";
  record.date = record.date || "";
  record.identifier = record.identifier || "";
  record.format = record.format || "";
  record.source = record.source || "";
  record.language = record.language || "";
  record.relation = record.relation || "";
  record.coverage = record.coverage || "";
  record.rights = record.rights || "";
  
  return record;
}

export default
class RecordsPane extends Component {
  
  state  = { 
    data: this.props.data,
    current: null
  };
  
  api = new RecordsApi();
  
  onInfo = (props) => {
    console.log("More info", props);
  }
  
  onEdit = (props) => {
    this.setState({data: this.props.data, current: assureRecord(props)});
  }
  
  onDelete = (props) => {
    this.props.onDelete(props.id);
  }
  
  onAdd = () => {
    this.setState({data: this.props.data, current: assureRecord()});
  }
  
  onSave = (record) => {
    this.props.onSave(record);
  }
  
  render(){

    let recordsTable = <div>
                          <RecordsTable records={this.state.data} onEdit={this.onEdit} onDelete={this.onDelete} onInfo={this.onInfo}/>
                          <Button type="button" icon="pi pi-plus" className="p-button-info add" 
                                  title="Add new record"
                                  onClick={this.onAdd}/>
                       </div>;
    let editorPane = <EditorPane onSave={this.onSave} record={this.state.current}/>;
    
    return(
      <div className="RecordsPane">
        {this.state.current? editorPane: recordsTable}
      </div>
    );
  }
}
