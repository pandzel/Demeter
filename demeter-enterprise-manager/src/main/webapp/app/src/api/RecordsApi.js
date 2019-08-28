import axios from 'axios';

export default
class RecordsApi {
  list() {
    return new Promise((resolve, reject) => {
      axios.get(`${config.context}rest/records`).then((result)=>resolve(result.data.data));
    });
  }
  
  create(data) {
    return new Promise((resolve, reject) => {
      axios.post(`${config.context}rest/records`, data==undefined? {}: data).then((result)=>resolve(result.data));
    });
  }
  
  delete(id) {
    return new Promise((resolve, reject) => {
      axios.delete(`${config.context}rest/records/${id}`).then((result)=>resolve(result.data));
    });
  }
  
  update(data) {
    return new Promise((resolve, reject) => {
      axios.put(`${config.context}rest/records/${data.id}`, data).then((result)=>resolve(result.data));
    });
  }
}