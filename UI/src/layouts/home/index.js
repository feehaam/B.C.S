// @mui material components
import Grid from "@mui/material/Grid";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";

// Material Dashboard 2 React example components
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import DashboardNavbar from "examples/Navbars/DashboardNavbar";
import Footer from "examples/Footer";
import ComplexStatisticsCard from "examples/Cards/StatisticsCards/ComplexStatisticsCard";

// Data
import { Link } from "react-router-dom";

function Home() {
  return (
    <DashboardLayout>
      <DashboardNavbar />
      <MDBox py={3}>
        <Grid
          container
          spacing={3}
          alignItems="center"
          justifyContent="center"
          flexDirection="column"
        >
          <Grid item xs={12} md={6} lg={3} style={{ width: "650px", maxWidth: "99%" }}>
            <Link to={"/questions"}>
              <MDBox mb={1.5}>
                <ComplexStatisticsCard
                  color="dark"
                  icon="pin"
                  title="BCS 10 - 45"
                  count={"All Questions"}
                  percentage={{
                    color: "success",
                    amount: "",
                    label: "Filter, sort, search, browse",
                  }}
                />
              </MDBox>
            </Link>
          </Grid>
          <Grid item xs={12} md={6} lg={3} style={{ width: "650px", maxWidth: "99%" }}>
            <Link to={"/set-tags-and-bans"}>
              <MDBox mb={1.5}>
                <ComplexStatisticsCard
                  icon="tag"
                  title="MCQ tagging"
                  count="Set Tags & Bans"
                  percentage={{
                    color: "success",
                    amount: "",
                    label: "Assign tags & set bans to individual questions",
                  }}
                />
              </MDBox>
            </Link>
          </Grid>
          <Grid item xs={12} md={6} lg={3} style={{ width: "650px", maxWidth: "99%" }}>
            <Link to={"/home"}>
              <MDBox mb={1.5}>
                <ComplexStatisticsCard
                  color="success"
                  icon="leaderboard"
                  title="Graphical data"
                  count="Statistics"
                  percentage={{
                    color: "success",
                    amount: "",
                    label: "See analysis reports and yearly stats",
                  }}
                />
              </MDBox>
            </Link>
          </Grid>
          <Grid item xs={12} md={6} lg={3} style={{ width: "650px", maxWidth: "99%" }}>
            <Link to={"/home"}>
              <MDBox mb={1.5}>
                <ComplexStatisticsCard
                  color="primary"
                  icon="?"
                  title="Personalized tests"
                  count="Test & Progress"
                  percentage={{
                    color: "success",
                    amount: "",
                    label: "Take test, see progress, weaknesses",
                  }}
                />
              </MDBox>
            </Link>
          </Grid>
        </Grid>
      </MDBox>
      <Footer />
    </DashboardLayout>
  );
}

export default Home;
