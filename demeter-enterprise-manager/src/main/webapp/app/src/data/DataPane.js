import React, { Component} from "react";
import "./DataPane.scss";
import RecordsApi from '../api/RecordsApi';
import RecordsTable from './RecordsTable';

export default
class DataPane extends Component{
  state = {};
  
  componentDidMount() {
    const api = new RecordsApi();
    api.list().then(result => {
      this.setState({
        data: result
      });
    });
  }
  
  onSave = (data) => {
    console.log("Saving", data);
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
