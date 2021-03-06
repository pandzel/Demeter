import axios from 'axios';

export default
class RecordsApi {
  list(page) {
    return new Promise((resolve, reject) => {
      axios.get(`${config.context}rest/records`+(page!==undefined? `?page=${page}`: ""))
              .then((result)=>{ console.log("RecordsApi list():", result.data); resolve(result.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
  
  create(data) {
    return new Promise((resolve, reject) => {
      axios.post(`${config.context}rest/records`, data==undefined? {}: data)
              .then((result)=>{ console.log("RecordsApi create():", result.data); resolve(result.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
  
  delete(id) {
    return new Promise((resolve, reject) => {
      axios.delete(`${config.context}rest/records/${id}`)
              .then((result)=>{ console.log("RecordsApi delete():", result.data); resolve(result.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
  
  update(data) {
    return new Promise((resolve, reject) => {
      axios.put(`${config.context}rest/records/${data.id}`, data)
              .then((result)=>{ console.log("RecordsApi update():", result.data); resolve(result.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
  
  listSets(recordId, page) {
    return new Promise((resolve, reject) => {
      axios.get(`${config.context}rest/records/${recordId}/sets`+(page!==undefined? `?page=${page}`: ""))
              .then((result)=>{ console.log("RecordsApi listSets():", result.data); resolve(result.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
  
  putCollection(setId, recordId) {
    return new Promise((resolve, reject) => {
      axios.put(`${config.context}rest/records/${recordId}/sets/${setId}`)
              .then((result)=>{ console.log("RecordsApi putCollection():", result.data); resolve(result.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
  
  delCollection(setId, recordId) {
    return new Promise((resolve, reject) => {
      axios.delete(`${config.context}rest/records/${recordId}/sets/${setId}`)
              .then((result)=>{ console.log("RecordsApi delCollection():", result.data); resolve(result.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
}