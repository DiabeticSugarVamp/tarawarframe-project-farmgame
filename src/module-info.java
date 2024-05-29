module PlowToProfit {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires javafx.media;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml;
}
