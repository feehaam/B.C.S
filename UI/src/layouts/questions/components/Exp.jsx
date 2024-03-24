import MDBox from "components/MDBox";
import { Button, Card } from "@mui/material";
import MDTypography from "components/MDTypography";
import { useState } from "react";
import LocalOfferIcon from "@mui/icons-material/LocalOffer";
import SubjectIcon from "@mui/icons-material/Subject";
import EventIcon from "@mui/icons-material/Event";
import TrendingUpIcon from "@mui/icons-material/TrendingUp";
/* eslint-disable */

const Exp = ({ mcq }) => {
  const isImageURL = (url) => {
    return /\.(gif|jpe?g|tiff?|png|webp|bmp)$/i.test(url);
  };
  const isLongExplanation = mcq.explanation.length > 100;
  const [showFullExplanation, setShowFullExplanation] = useState(false);
  const renderExplanation = () => {
    if (isImageURL(mcq.explanation)) {
      return +(
        <img
          src={mcq.explanation}
          alt="Explanation"
          style={{ width: "100%", maxHeight: "400px" }}
        />
      );
    } else {
      return (
        <div style={{ fontSize: "medium" }}>
          {showFullExplanation ? (
            <MDTypography variant="text" fontWeight="small">
              <b>Explanation:</b> {mcq.explanation}
            </MDTypography>
          ) : (
            <>
              <MDTypography variant="text" fontWeight="small">
                <b>Explanation:</b> {mcq.explanation.slice(0, 100)}
              </MDTypography>
              {isLongExplanation && "... "}
              {isLongExplanation && (
                <button
                  style={{
                    border: "1px solid #ddd",
                    padding: "2px",
                    borderRadius: "4px",
                  }}
                  onClick={() => setShowFullExplanation(!showFullExplanation)}
                >
                  {showFullExplanation ? "See less" : "See more"}
                </button>
              )}
            </>
          )}
        </div>
      );
    }
  };

  return (
    <MDBox pt={1} pb={2} px={2}>
      <MDBox component="ul" display="flex" flexDirection="column" p={0} m={0}>
        {renderExplanation()}
      </MDBox>
      <div style={{ marginTop: "8px" }}>
        <Button
          style={{ margin: "2px", padding: "5px" }}
          startIcon={
            <MDTypography variant="text" color="info" fontWeight="small">
              <SubjectIcon />
            </MDTypography>
          }
          variant="outlined"
        >
          <MDTypography variant="text" color="info" fontWeight="small">
            {mcq.subject}
          </MDTypography>
        </Button>
        <Button
          style={{ margin: "2px", padding: "5px" }}
          startIcon={
            <MDTypography variant="text" color="success" fontWeight="small">
              <EventIcon />
            </MDTypography>
          }
          variant="outlined"
        >
          <MDTypography variant="text" color="success" fontWeight="small">
            {mcq.year}
          </MDTypography>
        </Button>
        <Button
          style={{ margin: "2px", padding: "5px" }}
          startIcon={
            <MDTypography variant="text" color="primary" fontWeight="small">
              <TrendingUpIcon />
            </MDTypography>
          }
          variant="outlined"
        >
          <MDTypography variant="text" color="primary" fontWeight="small">
            {mcq.similarity}
          </MDTypography>
        </Button>
        {mcq.tags.map((tag, index) => (
          <Button
            style={{ margin: "2px", padding: "5px" }}
            key={index}
            startIcon={
              <MDTypography variant="text" fontWeight="small">
                <LocalOfferIcon />
              </MDTypography>
            }
            variant="outlined"
          >
            <MDTypography variant="text" fontWeight="small">
              {tag}
            </MDTypography>
          </Button>
        ))}
      </div>
    </MDBox>
  );
};

export default Exp;
