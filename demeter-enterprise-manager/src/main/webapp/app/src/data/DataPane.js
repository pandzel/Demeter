import React, { Component} from "react";
import "./DataPane.scss";
import RecordsApi from '../api/RecordsApi';
import RecordsTable from './RecordsTable';

export default
class DataPane extends Component{
  state = {};
  
  api = new RecordsApi();
  
  componentDidMount() {
    this.load();
  }
  
  onSave = (record) => {
    return new Promise((resolve, reject) => {
      if (record.id) {
        this.api.update(record).then(result => {
          resolve(result);
          this.load();
        }).catch(error => reject(error));
      } else {
        this.api.create(record).then(result => {
          resolve(result);
          this.load();
        }).catch(error => reject(error));
      }
    });
  }
  
  onDelete = (id) => {
    return new Promise((resolve, reject) => {
        this.api.delete(id).then(result => {
          resolve(result);
          this.load();
        }).catch(error => reject(error));
    });
  }
  
  load = () => {
    this.setState({ data: null });
    this.api.list().then(result => {
      console.log("Loaded data", result);
      this.setState({ data: result });
    });
  }
  
  render(){
    return(
      <div className="DataPane">
        <div className="Title">Data</div>
        {this.state.data && <RecordsTable onDelete={this.onDelete} onSave={this.onSave} data={this.state.data}/>}
      </div>
    );
  }
}
