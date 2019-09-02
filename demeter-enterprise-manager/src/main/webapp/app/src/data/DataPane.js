import React, { Component} from "react";
import "./DataPane.scss";
import RecordsApi from '../api/RecordsApi';
import RecordsPane from './RecordsPane';

export default
class DataPane extends Component{
  state = {};
  
  api = new RecordsApi();
  
  componentDidMount() {
    this.load();
  }
  
  onSave = (record) => {
    return new Promise((resolve, reject) => {
      (record.id? this.api.update: this.api.create)(record).then(resolve).catch(reject);
    }).then(() => {
      return this.load();
    });
  }
  
  onDelete = (id) => {
    return new Promise((resolve, reject) => {
      this.api.delete(id).then(resolve).catch(reject);
    }).then(() => {
      return this.load();
    });
  }
  
  load = () => {
    return new Promise((resolve, reject) => {
      this.api.list().then(result => {
        console.log("Loaded data", result);
        this.setState({ data: result });
        resolve(result);
      }).catch(reject);
    });
  }
  
  render(){
    return(
      <div className="DataPane">
        <div className="Title">Data</div>
        {this.state.data && <RecordsPane data={this.state.data} onDelete={this.onDelete} onSave={this.onSave}/>}
      </div>
    );
  }
}
