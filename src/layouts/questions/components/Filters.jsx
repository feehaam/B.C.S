import { useEffect, useState } from "react";
import AxiosInstance from "scripts/axioInstance";

const Filters = ({ applyFilter }) => {
  const [params, setParams] = useState();
  const [load, setLoad] = useState(true);

  useEffect(() => {
    setLoad(!load);
  }, []);

  useEffect(() => {
    AxiosInstance.get("http://localhost:8000/unified/filters")
      .then((response) => {
        setParams(response.data);
        console.log(response);
      })
      .catch((error) => {
        console.log(error);
      });
  }, [load]);

  return (
    <>
      <select
        style={{
          backgroundColor: "rgba(0, 0, 50, 0.5)",
          color: "white",
          margin: "2px",
          padding: "5px",
          borderRadius: "5px",
        }}
        onChange={(event) => applyFilter("year", event.target.value, 1)}
      >
        <option>Select Year</option>
        <option>All</option>
        {params !== undefined ? (
          params.years.map((item, index) => {
            return (
              <option id={index} key={index}>
                {item + " (" + (item + 1978) + ")"}
              </option>
            );
          })
        ) : (
          <option>Loading subject...</option>
        )}
      </select>
      <select
        style={{
          backgroundColor: "rgba(0, 0, 50, 0.5)",
          color: "white",
          margin: "2px",
          padding: "5px",
          borderRadius: "5px",
        }}
        onChange={(event) => applyFilter("subject", event.target.value, 1)}
      >
        <option>Select Subject</option>
        <option>All</option>
        {params !== undefined ? (
          params.subjects.map((item, index) => {
            return (
              <option id={index} key={index}>
                {item}
              </option>
            );
          })
        ) : (
          <option>Loading subject...</option>
        )}
      </select>
      <select
        style={{
          backgroundColor: "rgba(0, 0, 50, 0.5)",
          color: "white",
          margin: "2px",
          padding: "5px",
          borderRadius: "5px",
        }}
        onChange={(event) => applyFilter("sortBy", event.target.value, 1)}
      >
        <option>Sort By</option>
        <option>None</option>
        {params !== undefined ? (
          params.sortBy.map((item, index) => {
            return (
              <option id={index} key={index}>
                {item}
              </option>
            );
          })
        ) : (
          <option>Loading subject...</option>
        )}
      </select>
      <select
        style={{
          backgroundColor: "rgba(0, 0, 50, 0.5)",
          color: "white",
          margin: "2px",
          padding: "5px",
          borderRadius: "5px",
        }}
        onChange={(event) => {
          const tag = event.target.value;
          applyFilter("tags", tag, 1);
        }}
      >
        <option>Add Tag</option>
        {params !== undefined ? (
          params.tags.map((item, index) => {
            return (
              <option id={index} key={index}>
                {item}
              </option>
            );
          })
        ) : (
          <option>Loading subject...</option>
        )}
      </select>
    </>
  );
};

export default Filters;
