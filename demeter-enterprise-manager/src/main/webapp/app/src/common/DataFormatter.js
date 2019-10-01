import React from "react";

function formatData(data, cols, keygen, factory) {
  if (!data) return null;
  
  const COLS = cols || 2;
  
  let rowsOfData = data.reduce((acc,record,idx) => {
    let pos = Math.floor(idx/COLS);
    while (acc.length<=pos)
      acc.push([]);
    acc[pos].push(record);
    return acc;
  }, []);
  
  let rowsOfRecords = rowsOfData.map((row, ridx) => 
    <div key={`row-${ridx}`} style={{display: "table-row"}}>
      {row.map(record => (
        <div key={`cell-${keygen(record)}`} className="recordCell" style={{display: "table-cell"}}>
          {factory(record)}
        </div>
      ))}
    </div>
  );
  
  return rowsOfRecords;
}

export default formatData;