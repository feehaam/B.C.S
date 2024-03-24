// @mui material components
import Grid from "@mui/material/Grid";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";

// Material Dashboard 2 React examples
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import DashboardNavbar from "examples/Navbars/DashboardNavbar";
import Footer from "examples/Footer";

// Billing page components
import { Card } from "@mui/material";
import MDTypography from "components/MDTypography";
import Options from "./components/Options";
import { useEffect, useState } from "react";
import AxiosInstance from "scripts/axioInstance";
import Exp from "./components/Exp";

function Questions() {
  const [questions, setQuestions] = useState([]);
  const [warning, setWarning] = useState("");

  useEffect(() => {
    const fetchData = async () => {
      AxiosInstance.get("http://localhost:8000/unified?pageNo=0&pageSize=30")
        .then((response) => {
          console.log(response);
          setQuestions(response.data.results);
        })
        .catch((error) => {
          setWarning(error);
        });
    };

    fetchData();
  }, []);

  return (
    <DashboardLayout>
      <DashboardNavbar absolute isMini />
      <MDBox mt={8}>
        <MDBox mb={3}>
          <Grid container spacing={3}>
            <Grid item xs={12} md={7}>
              {questions.map((q) => (
                <Card key={q.id} style={{ marginBottom: "10px" }}>
                  <MDBox pt={3} px={2}>
                    <MDTypography variant="h6" fontWeight="medium">
                      {q.question}
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
      <Footer />
    </DashboardLayout>
  );
}

export default Questions;
