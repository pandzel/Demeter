import axios from 'axios';

export default
class SetsApi {
  list(page) {
    return new Promise((resolve, reject) => {
      axios.get(`${config.context}rest/sets`+(page? `?page=${page}`: ""))
              .then((result)=>{ console.log("SetsApi list():", result.data); resolve(result.data.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
  
  create(data) {
    return new Promise((resolve, reject) => {
      axios.post(`${config.context}rest/sets`, data==undefined? {}: data)
              .then((result)=>{ console.log("SetsApi create():", result.data); resolve(result.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
  
  delete(id) {
    return new Promise((resolve, reject) => {
      axios.delete(`${config.context}rest/sets/${id}`)
              .then((result)=>{ console.log("SetsApi delete():", result.data); resolve(result.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
  
  update(data) {
    return new Promise((resolve, reject) => {
      axios.put(`${config.context}rest/sets/${data.id}`, data)
              .then((result)=>{ console.log("SetsApi update():", result.data); resolve(result.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
}