// @mui material components
import Grid from "@mui/material/Grid";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";

// Material Dashboard 2 React examples
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import DashboardNavbar from "examples/Navbars/DashboardNavbar";
import Footer from "examples/Footer";

// Billing page components
import { Button, Card } from "@mui/material";
import MDTypography from "components/MDTypography";
import Options from "./components/Options";
import { useEffect, useState } from "react";
import AxiosInstance from "scripts/axioInstance";
import Exp from "./components/Exp";
import Filters from "./components/Filters";
import Tag from "./components/Tag";
import MDButton from "components/MDButton";
import Loader from "layouts/loader/Loader";

function Questions() {
  const [questions, setQuestions] = useState([]);
  const [tags, setTags] = useState([]);
  const [filter, setFilter] = useState({});
  const [searchValue, setSearchValue] = useState("");

  const fetchData = async (url) => {
    setQuestions([]);
    AxiosInstance.get(url)
      .then((response) => {
        console.log(response);
        setQuestions(response.data.results);
      })
      .catch((error) => {});
  };

  useEffect(() => {
    fetchData("http://localhost:8000/unified?pageNo=1&pageSize=200");
  }, []);

  function applyFilter(type, value, action) {
    if (type === "tags") {
      if (value === "Add Tag") return;
      const uniqueTags = new Set(tags);
      if (!uniqueTags.has(value)) {
        setTags((prevTags) => [...prevTags, value]);
        if (filter.tags === undefined) {
          filter.tags = [];
        }
        const updatedFilter = { ...filter, tags: [...filter.tags, value] };
        setFilter(updatedFilter);
        return;
      }
    } else if (type === "subject" && (value === "All" || value === "Select Subject")) {
      value = null;
    } else if (type === "year" && (value === "All" || value === "Select Year")) {
      value = null;
    } else if (type === "sortBy" && (value === "None" || value === "Sort By")) {
      value = null;
    }
    const existing = filter;
    existing[type] = value;
    setFilter(existing);
  }

  const handleTagRemove = (tagToRemove) => {
    const updated = [];
    for (let i = 0; i < tags.length; i++) {
      if (tags[i] !== tagToRemove) {
        updated[updated.length] = tags[i];
      }
    }
    setTags(updated);
    const existing = filter;
    existing.tags = updated;
    setFilter(existing);
  };

  const submit = () => {
    // Prepare the URL with filter parameters
    let url = "http://localhost:8000/unified?";
    for (const key in filter) {
      if (filter[key] !== null && filter[key] !== undefined) {
        if (key === "year") {
          // Extract the year from the value inside the brackets
          const year = filter[key].match(/\(([^)]+)\)/)[1];
          url += `year=${year}&`;
        } else {
          url += `${key}=${filter[key]}&`;
        }
      }
    }
    url = url.slice(0, -1); // Remove the last '&' character

    console.log("Submit URL:", url);

    // Fetch data using the constructed URL
    fetchData(url);
  };

  const reset = () => {
    window.location.reload();
  };

  const handleSearchChange = (event) => {
    const { value } = event.target;
    setSearchValue(value);
    filter.search = value;
    if (value === "") {
      filter.search = null;
    }
  };

  return (
    <DashboardLayout>
      <DashboardNavbar />
      <div
        style={{
          maxWidth: "1100px",
          margin: "auto",
        }}
      >
        <MDBox mt={0}>
          <MDBox mb={3}>
            <Grid container spacing={3}>
              <Grid
                item
                xs={12}
                md={7}
                style={{
                  margin: "auto",
                }}
              >
                <Card style={{ marginBottom: "10px" }}>
                  <MDTypography px={2} variant="h4" fontWeight="medium">
                    Filters
                  </MDTypography>
                  <MDBox px={2} display="flex" justifyContent="center" flexWrap="wrap">
                    <Filters applyFilter={applyFilter} />
                  </MDBox>
                  <MDBox px={2} display="flex" justifyContent="center" flexWrap="wrap">
                    <input
                      type="text"
                      placeholder="Search"
                      style={{
                        width: "80%",
                        padding: "5px",
                        border: "1px solid #ccc",
                        boxShadow: "2px 2px 4px rgba(0, 0, 0, 0.4)",
                        borderRadius: "5px",
                        color: "#bb0000",
                        fontWeight: "bold",
                        fontFamily: "cursive",
                      }}
                      value={searchValue}
                      onChange={handleSearchChange}
                    />
                  </MDBox>
                  <MDBox px={2} display="flex" justifyContent="center" flexWrap="wrap">
                    <div>
                      {tags !== undefined && tags.length > 0 && (
                        <>
                          <small>Selected Tags</small>
                          {tags.map((tag, index) => (
                            <Tag key={index} tag={tag} onRemove={handleTagRemove} />
                          ))}
                        </>
                      )}
                    </div>
                  </MDBox>
                  <MDBox display="flex" justifyContent="center" flexWrap="wrap">
                    <Button
                      onClick={() => {
                        reset();
                      }}
                    >
                      <MDButton
                        variant="contained"
                        size="small"
                        style={{ marginTop: "3px" }}
                        color={"primary"}
                      >
                        Reset
                      </MDButton>
                    </Button>
                    <Button
                      onClick={() => {
                        submit();
                      }}
                    >
                      <MDButton
                        variant="contained"
                        size="small"
                        style={{ marginTop: "3px" }}
                        color={"info"}
                      >
                        Apply
                      </MDButton>
                    </Button>
                  </MDBox>
                </Card>
                <Card style={{ marginBottom: "10px" }}>
                  <MDTypography px={2} variant="h6" fontWeight="medium">
                    {questions.length > 0 ? (
                      <div style={{ margin: "10px" }}>Showing MCQ in total: {questions.length}</div>
                    ) : (
                      <>
                        <Loader />
                      </>
                    )}
                  </MDTypography>
                </Card>
                {questions.map((q) => (
                  <Card key={q.id} style={{ marginBottom: "10px" }}>
                    <MDBox pt={3} px={2}>
                      <MDTypography variant="h6" fontWeight="medium">
                        {q.id}. {q.question}
                      </MDTypography>
                    </MDBox>
                    <MDBox pt={1} pb={1} px={2}>
                      <MDBox component="ul" display="flex" flexDirection="column" p={0} m={0}>
                        <Options
                          a={q.optionA}
                          b={q.optionB}
                          c={q.optionC}
                          d={q.optionD}
                          correct={q.answer}
                        />
                      </MDBox>
                    </MDBox>
                    <Exp mcq={q} />
                  </Card>
                ))}
              </Grid>
            </Grid>
          </MDBox>
        </MDBox>
      </div>
      <Footer />
    </DashboardLayout>
  );
}

export default Questions;
