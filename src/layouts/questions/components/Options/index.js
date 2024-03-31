import PropTypes from "prop-types";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";

// Material Dashboard 2 React context
import { useMaterialUIController } from "context";
import { Button, Icon, IconButton } from "@mui/material";
import MDButton from "components/MDButton";
import { useState } from "react";
import MDTypography from "components/MDTypography";

function Options({ a, b, c, d, correct, noGutter }) {
  const [controller] = useMaterialUIController();
  const { darkMode } = controller;
  const [showCorrect, setShowCorrect] = useState(false);

  return (
    <MDBox
      component="li"
      display="flex"
      justifyContent="space-between"
      alignItems="flex-start"
      bgColor={darkMode ? "transparent" : "grey-100"}
      borderRadius="lg"
      p={3}
      mt={2}
    >
      <MDBox width="100%" display="flex" flexDirection="column" mb={-2}>
        <MDBox width="100%" display="flex" flexDirection="row">
          <MDButton
            variant={showCorrect && correct == "1" ? "contained" : "outlined"}
            style={{ marginRight: "4px", marginBottom: "3px" }}
            color={"info"}
            size="small"
            iconOnly
            circular
          >
            <Icon sx={{ fontWeight: "bold", marginBottom: "5px" }}>
              <MDTypography variant="text" fontWeight="small">
                A
              </MDTypography>
            </Icon>
          </MDButton>{" "}
          <div style={{ fontSize: "medium" }}>
            <MDTypography variant="text" fontWeight="small">
              {a}
            </MDTypography>
          </div>
        </MDBox>
        <MDBox width="100%" display="flex" flexDirection="row">
          <MDButton
            variant={showCorrect && correct == "2" ? "contained" : "outlined"}
            style={{ marginRight: "4px", marginBottom: "3px" }}
            color={"info"}
            size="small"
            iconOnly
            circular
          >
            <Icon sx={{ fontWeight: "bold", marginBottom: "5px" }}>
              <MDTypography variant="text" fontWeight="small">
                B
              </MDTypography>
            </Icon>
          </MDButton>{" "}
          <div style={{ fontSize: "medium" }}>
            <MDTypography variant="text" fontWeight="small">
              {b}
            </MDTypography>
          </div>
        </MDBox>
        <MDBox width="100%" display="flex" flexDirection="row">
          <MDButton
            variant={showCorrect && correct == "3" ? "contained" : "outlined"}
            style={{ marginRight: "4px", marginBottom: "3px" }}
            color={"info"}
            size="small"
            iconOnly
            circular
          >
            <Icon sx={{ fontWeight: "bold", marginBottom: "5px" }}>
              <MDTypography variant="text" fontWeight="small">
                C
              </MDTypography>
            </Icon>
          </MDButton>{" "}
          <div style={{ fontSize: "medium" }}>
            <MDTypography variant="text" fontWeight="small">
              {c}
            </MDTypography>
          </div>
        </MDBox>
        <MDBox width="100%" display="flex" flexDirection="row">
          <MDButton
            variant={showCorrect && correct == "4" ? "contained" : "outlined"}
            size="small"
            style={{ marginRight: "4px", marginBottom: "3px" }}
            color={"info"}
            iconOnly
            circular
          >
            <Icon sx={{ fontWeight: "bold", marginBottom: "5px" }}>
              <MDTypography variant="text" fontWeight="small">
                D
              </MDTypography>
            </Icon>
          </MDButton>{" "}
          <div style={{ fontSize: "medium" }}>
            <MDTypography variant="text" fontWeight="small">
              {d}
            </MDTypography>
          </div>
        </MDBox>
        <Button
          style={{ width: "100%" }}
          onClick={() => {
            setShowCorrect(!showCorrect);
          }}
        >
          <MDButton variant="outlined" size="small" style={{ marginTop: "3px" }} color={"info"}>
            <MDTypography variant="text" fontWeight="small">
              {showCorrect ? "Hide correct answer" : "Show correct answer"}
            </MDTypography>
          </MDButton>
        </Button>
      </MDBox>
    </MDBox>
  );
}

// Setting default values for the props of Bill
Options.defaultProps = {
  noGutter: false,
};

// Typechecking props for the Bill
Options.propTypes = {
  a: PropTypes.string.isRequired,
  b: PropTypes.string.isRequired,
  c: PropTypes.string.isRequired,
  d: PropTypes.string.isRequired,
  correct: PropTypes.string.isRequired,
  noGutter: PropTypes.bool,
};

export default Options;
