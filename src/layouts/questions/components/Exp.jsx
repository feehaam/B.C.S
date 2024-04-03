import MDBox from "components/MDBox";
import {
  Button,
  Card,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  IconButton,
  Typography,
} from "@mui/material";
import MDTypography from "components/MDTypography";
import { useState } from "react";
import LocalOfferIcon from "@mui/icons-material/LocalOffer";
import SubjectIcon from "@mui/icons-material/Subject";
import EventIcon from "@mui/icons-material/Event";
import TrendingUpIcon from "@mui/icons-material/TrendingUp";
import ResetIcon from "@mui/icons-material/Restore";
import AxiosInstance from "scripts/axioInstance";

const Exp = ({ mcq }) => {
  const isImageURL = (url) => {
    return /\.(gif|jpe?g|tiff?|png|webp|bmp)$/i.test(url);
  };
  const isLongExplanation = mcq.explanation.length > 100;
  const [showFullExplanation, setShowFullExplanation] = useState(false);

  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
  const handleOpenConfirmDialog = () => setOpenConfirmDialog(true);
  const handleCloseConfirmDialog = () => setOpenConfirmDialog(false);
  const handleConfirmReset = () => {
    removeTags();
    handleCloseConfirmDialog();
  };

  const removeTags = () => {
    const url = "https://bcs-game.azurewebsites.net/unified/" + mcq.id;
    AxiosInstance.delete(url)
      .then((response) => {
        console.log(response);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const renderExplanation = () => {
    if (mcq.explanation.includes("http")) {
      return (
        <div style={{ fontSize: "medium" }}>
          <MDTypography variant="text" fontWeight="small">
            <b>Explanation:</b>
          </MDTypography>
          <img
            src={mcq.explanation}
            alt="Explanation"
            style={{ width: "100%", maxHeight: "400px" }}
          />
        </div>
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
                    cursor: "pointer",
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
        {mcq.tags !== undefined && mcq.tags !== null && (
          <>
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
                  {tag.word}
                </MDTypography>
              </Button>
            ))}
          </>
        )}
        <IconButton onClick={handleOpenConfirmDialog}>
          <ResetIcon />
        </IconButton>
        <Dialog open={openConfirmDialog} onClose={handleCloseConfirmDialog}>
          <DialogTitle>
            <Typography variant="h6">Confirm Reset</Typography>
          </DialogTitle>
          <DialogContent>
            <Typography>Are you sure you want to reset the tags for this MCQ?</Typography>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseConfirmDialog}>Cancel</Button>
            <Button onClick={handleConfirmReset}>Confirm</Button>
          </DialogActions>
        </Dialog>
      </div>
    </MDBox>
  );
};

export default Exp;
