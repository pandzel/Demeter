import React, { Component} from "react";
import "./DataPane.scss";
import RecordsApi from '../api/RecordsApi';
import RecordsTable from './RecordsTable';

export default
class DataPane extends Component{
  state = {};
  
  api = new RecordsApi();
  
  componentDidMount() {
    const api = new RecordsApi();
    api.list().then(result => {
      this.setState({
        data: result
      });
    });
  }
  
  onSave = (record) => {
    const rowData = record;
    if (record.id) {
      this.api.update(record).then(result => {
        console.log("Update (modify)", record);
      });
    } else {
      this.api.create(record).then(result => {
        console.log("Update (insert)", record);
      });
    }
  }
  
  render(){
    return(
      <div className="DataPane">
        <div className="Title">Data</div>
        {this.state.data && <RecordsTable onSave={this.onSave} data={this.state.data}/>}
      </div>
    );
  }
}
