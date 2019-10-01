import React, { Component} from "react";
import "./Data.scss";
import {Button} from 'primereact/button';
import {Paginator} from 'primereact/paginator';
import SetsApi from '../api/SetsApi';
import DataApi from '../api/RecordsApi';
import RecordSet from './RecordSet';
import formatData from '../common/DataFormatter';

export default
class RecordSets extends Component {
  state = {
    sets: {
      page: 0,
      pageSize: 0,
      total: 0,
      data: []
    }
  };
  
  setsApi = new SetsApi();
  dataApi = new DataApi();
  
  componentDidMount() {
    this.load();
  }
  
  load = (page) => {
    this.setsApi.list().then(allSets => {
      this.listSets().then(dataSets => {
        allSets.data.forEach(set => {
          set.checked = !!dataSets.find(ds => ds.key === set.id );
        });
        this.setState({sets: allSets});
      }).catch(this.props.onError);
    }).catch(this.props.onError);
  }
  
  listSets = (page) => {
    return new Promise((resolve, reject) => {
      this.dataApi.listSets(this.props.record.id, page).then(dataSets => {
        var data = dataSets.data;
        if (data && data.length >= dataSets.pageSize) {
          this.listSets(dataSets.page+1)
                  .then(result => resolve([...data, ...result.data]))
                  .catch(reject);
        } else {
          resolve(data);
        }
      }).catch(reject);
    });
  }
  
  onCheck = (id, check) => {
    let mutator = check? this.dataApi.putCollection: this.dataApi.delCollection;
    mutator(id, this.props.record.id).then(result => {
      let newSets = {...this.state.sets};
      newSets.data.forEach(d => {
        if (d.id===id) {
          d.checked = check;
        }
      });
      this.setState({sets: newSets});
    }).catch(this.props.onError);
  }

  render(){
    let rowsOfRecords = formatData(this.state.sets.data, this.props.cols, 
                        (set) => set.id,
                        (set) => <RecordSet key={set.id} set={set} onCheck={(check) => this.onCheck(set.id, check)}/>);
    
    return(
      <div className="RecordSetsList">
        <div className="RecordSets">
        <Paginator first={this.state.sets.page * this.state.sets.pageSize} rows={this.state.sets.pageSize} totalRecords={this.state.sets.total} onPageChange={(e) => this.load(e.page)}></Paginator>
        {rowsOfRecords}
        </div>
        <Button label="Back" onClick={(e) => this.props.onExit()}/>
      </div>
    );
  }
}