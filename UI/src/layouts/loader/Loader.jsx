import loading1 from "assets/loading/loading1.gif";
import loading2 from "assets/loading/loading2.gif";
import loading3 from "assets/loading/loading3.gif";
import loading4 from "assets/loading/loading4.gif";
import MDAvatar from "components/MDAvatar";
import MDTypography from "components/MDTypography";
import React from "react";

const Loader = () => {
  const loadingImages = [loading1, loading2, loading3, loading4];

  return (
    <div style={{ display: "flex", flexDirection: "row", alignItems: "center" }}>
      <div>
        <MDAvatar
          src={loadingImages[Math.floor(Math.random() * loadingImages.length)]}
          alt="loading-avatar"
          size="xl"
          shadow="sm"
          style={{ margin: "10px" }}
        />
      </div>
      <div>
        <MDTypography px={2} style={{ color: "#777" }} variant="h3" fontWeight="medium">
          Loading . . .
        </MDTypography>
      </div>
    </div>
  );
};

export default Loader;
