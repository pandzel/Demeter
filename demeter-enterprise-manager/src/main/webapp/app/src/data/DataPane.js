import React, { Component} from "react";
import "./DataPane.scss";
import RecordsApi from '../api/RecordsApi';
import DataTable from './DataTable';

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
  
  render(){
    return(
      <div className="DataPane">
        <div className="Title">Data</div>
        {this.state.data && <DataTable data={this.state.data}/>}
      </div>
    );
  }
}
