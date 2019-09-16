import React, {Component} from "react";
import "./DataPane.scss";

import {Button} from 'primereact/button';
import {Paginator} from 'primereact/paginator';

import RecordsApi from '../api/RecordsApi';
import EditorPane from '../common/EditorPane';
import RecordsTable from './RecordsTable';
import SetsList from './SetsList';

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
    current: null,
    sets: null
  };
  
  api = new RecordsApi();
  
  onInfo = (props) => {
    this.api.listSets(props.id).then(result => this.setState({sets: result, current: props}));
  }
  
  onEdit = (props) => {
    this.setState({current: assureRecord(props)});
  }
  
  onDelete = (props) => {
    this.props.onDelete(props.id);
  }
  
  onAdd = () => {
    this.setState({current: assureRecord()});
  }
  
  onSave = (record) => {
    this.props.onSave(record).then(()=>{
      this.setState({current: null});
    });
  }
  
  onCancel = (record) => {
    this.setState({current: null, sets: null});
  }
  
  onPageChange = (page) => {
    this.props.onPageChange(page);
  }
  
  render(){
    let view = <div>
                <Paginator first={this.state.data.page * this.state.data.pageSize} rows={this.state.data.pageSize} totalRecords={this.state.data.total} onPageChange={(e) => this.onPageChange(e.page)}></Paginator>
                <RecordsTable records={this.state.data.data} onEdit={this.onEdit} onDelete={this.onDelete} onInfo={this.onInfo}/>
                <Button type="button" icon="pi pi-plus" className="p-button-info add" 
                        title="Add new record"
                        onClick={this.onAdd}/>
               </div>;

    if (this.state.sets) {
      view = <SetsList record={this.state.current} sets={this.state.sets} onCancel={this.onCancel} />;
    } else if (this.state.current) {
      view = <EditorPane onSave={this.onSave} onCancel={this.onCancel} record={this.state.current} />;
    }
      
    return(
      <div className="RecordsPane">
        {view}
      </div>
    );
  }
}
