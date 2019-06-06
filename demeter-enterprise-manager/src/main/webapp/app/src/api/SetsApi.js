import axios from 'axios';

export default
class SetsApi {
  list() {
    return new Promise((resolve, reject) => {
      axios.get(`${config.context}rest/sets`).then((result)=>resolve(result.data));
    });
  }
  
  create(data) {
    return new Promise((resolve, reject) => {
      axios.post(`${config.context}rest/sets`, data==undefined? {}: data).then((result)=>resolve(result.data));
    });
  }
  
  delete(id) {
    return new Promise((resolve, reject) => {
      axios.delete(`${config.context}rest/sets/`+id).then((result)=>resolve(result.data));
    });
  }
  
  update(data) {
    return new Promise((resolve, reject) => {
      axios.put(`${config.context}rest/sets/`+data.id, data).then((result)=>resolve(result.data));
    });
  }
}