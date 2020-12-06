package com.model.other;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation Color.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class Color implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<String> colores;
	private List<String> bordes;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public Color() {
		this.color();
		this.bordes();
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que inicializa los colores a usar.
	 */
	public void color() {
		this.colores = new ArrayList<String>();
		this.colores.add("rgba(255, 99, 132, 0.2)");
		this.colores.add("rgba(255, 159, 64, 0.2)");
		this.colores.add("rgba(255, 205, 86, 0.2)");
		this.colores.add("rgba(75, 192, 192, 0.2)");
		this.colores.add("rgba(54, 162, 235, 0.2)");
		this.colores.add("rgba(153, 102, 255, 0.2)");
		this.colores.add("rgba(201, 203, 207, 0.2)");
		this.colores.add("rgba(199, 149, 149, 0.2)");  
		this.colores.add("rgba(224, 52, 52, 0.2)");
		this.colores.add("rgba(86, 67, 117, 0.2)");
		this.colores.add("rgba(120, 252, 3, 0.2)");
		this.colores.add("rgba(40, 200, 152, 0.2)");
		this.colores.add("rgba(76, 134, 151, 0.2)");
		this.colores.add("rgba(0, 128, 0, 0.2)");
		this.colores.add("rgba(102, 205, 170, 0.2)");
		this.colores.add("rgba(227, 119, 97, 0.2)");
		this.colores.add("rgba(220, 220, 220, 0.2)");
		this.colores.add("rgba(233, 150, 122, 0.2)");
		this.colores.add("rgba(241, 7, 7, 0.2)");
		this.colores.add("rgba(8, 253, 179, 0.2)");
		this.colores.add("rgba(82, 229, 184, 0.2)");
		this.colores.add("rgba(77, 225, 120, 0.2)");
		this.colores.add("rgba(248, 61, 61, 0.2)");
		this.colores.add("rgba(189, 159, 238, 0.2)");
		this.colores.add("rgba(238, 130, 238, 0.2)");
		this.colores.add("rgba(198, 37, 37, 0.2)");
		this.colores.add("rgba(156, 7, 7, 0.2)");
		this.colores.add("rgba(246, 122, 122, 0.2)");
		this.colores.add("rgba(246, 168, 168, 0.2)");
		this.colores.add("rgba(55, 4, 140, 0.2)");
		this.colores.add("rgba(107, 65, 176, 0.2)");
		this.colores.add("rgba(246, 48, 8, 0.2)");
		this.colores.add("rgba(221, 45, 10, 0.2)");
		this.colores.add("rgba(220, 66, 35, 0.2)");
		this.colores.add("rgba(152, 135, 30, 0.2)");
		this.colores.add("rgba(165, 37, 11, 0.2)");
		this.colores.add("rgba(253, 92, 60, 0.2)");
		this.colores.add("rgba(243, 226, 118, 0.2)");  
		this.colores.add("rgba(97, 8, 243, 0.2)");
		this.colores.add("rgba(62, 176, 141, 0.2)");
		this.colores.add("rgba(161, 252, 224, 0.2)");
		this.colores.add("rgba(180, 75, 113, 0.2)");
		this.colores.add("rgba(206, 187, 67, 0.2)");
		this.colores.add("rgba(16, 155, 112, 0.2)");
		this.colores.add("rgba(160, 120, 225, 0.2)");
		this.colores.add("rgba(167, 122, 240, 0.2)");
		this.colores.add("rgba(255, 231, 76, 0.2)");
		this.colores.add("rgba(251, 219, 15, 0.2)");
		this.colores.add("rgba(95,158,160, 0.2)");
		this.colores.add("rgba(147,112,219, 0.2)");
		this.colores.add("rgba(153,50,204, 0.2)");
		this.colores.add("rgba(240,230,140, 0.2)");
		this.colores.add("rgba(203, 176, 6, 0.2)");
		this.colores.add("rgba(155, 242, 78, 0.2)");
		this.colores.add("rgba(138, 203, 81, 0.2)");
		this.colores.add("rgba(187, 235, 144, 0.2)");
		this.colores.add("rgba(110, 199, 172, 0.2)");
		this.colores.add("rgba(193, 247, 230, 0.2)");   
		this.colores.add("rgba(255, 2, 94, 0.2)");
		this.colores.add("rgba(137, 9, 56, 0.2)");
		this.colores.add("rgba(210, 49, 107, 0.2)");
		this.colores.add("rgba(247, 99, 153, 0.2)");
		this.colores.add("rgba(229, 110, 153, 0.2)");
		this.colores.add("rgba(196, 138, 159, 0.2)");
		this.colores.add("rgba(21, 118, 89, 0.2)");
		this.colores.add("rgba(55, 4, 140, 0.2)");
		this.colores.add("rgba(234, 197, 210, 0.2)");    
		this.colores.add("rgba(135, 48, 234, 0.2)");
		this.colores.add("rgba(172, 103, 249, 0.2)");
		this.colores.add("rgba(161, 111, 218, 0.2)");
		this.colores.add("rgba(93, 185, 11, 0.2)");  
		this.colores.add("rgba(65, 129, 9, 0.2)");
		this.colores.add("rgba(114, 194, 42, 0.2)");
		this.colores.add("rgba(16, 138, 175, 0.2)");
		this.colores.add("rgba(49, 161, 194, 0.2)");
		this.colores.add("rgba(1, 197, 255, 0.2)");
		this.colores.add("rgba(55, 4, 140, 0.2)");
		this.colores.add("rgba(142, 226, 250, 0.2)");
		this.colores.add("rgba(201, 158, 250, 0.2)");
		this.colores.add("rgba(226, 203, 251, 0.2)");
		this.colores.add("rgba(183, 174, 194, 0.2)"); 
		this.colores.add("rgba(45, 108, 127, 0.2)");
		this.colores.add("rgba(2, 104, 134, 0.2)");
		this.colores.add("rgba(143, 213 234, 0.2)");
		this.colores.add("rgba(129, 22, 250, 0.2)");
		this.colores.add("rgba(85, 20, 158, 0.2)");
		this.colores.add("rgba(116, 58, 183, 0.2)");
		this.colores.add("rgba(165, 198, 208, 0.2)");
		this.colores.add("rgba(155, 209, 225, 0.2)");   
		this.colores.add("rgba(113, 73, 158, 0.2)");
		this.colores.add("rgba(17, 240, 81, 0.2)");
		this.colores.add("rgba(10, 206, 67, 0.2)");
		this.colores.add("rgba(66, 255, 121, 0.2)");
		this.colores.add("rgba(214, 37, 203, 0.2)");
		this.colores.add("rgba(130, 62, 126, 0.2)");
		this.colores.add("rgba(157, 112, 154, 0.2)");
		this.colores.add("rgba(7, 138, 45, 0.2)");
		this.colores.add("rgba(33, 100, 53, 0.2)");
		this.colores.add("rgba(104, 238, 143, 0.2)");
		this.colores.add("rgba(143, 244, 172, 0.2)");
		this.colores.add("rgba(137, 187, 152, 0.2)");
		this.colores.add("rgba(192, 234, 204, 0.2)"); 
		this.colores.add("rgba(128, 25, 122, 0.2)");
		this.colores.add("rgba(167, 15, 158, 0.2)");
		this.colores.add("rgba(202, 33, 192, 0.2)");   
		this.colores.add("rgba(255, 0, 0, 0.2)");
		this.colores.add("rgba(0, 0, 255, 0.2)");
		this.colores.add("rgba(60, 179, 113, 0.2)");
		this.colores.add("rgba(238, 130, 238, 0.2)");
		this.colores.add("rgba(255, 165, 0, 0.2)");
		this.colores.add("rgba(106, 90, 205, 0.2)");
		this.colores.add("rgba(0, 255, 0, 0.2)");
		this.colores.add("rgba(255, 255, 0, 0.2)");
		this.colores.add("rgba(0, 255, 255, 0.2)");
		this.colores.add("rgba(255, 0, 255, 0.2)");
		this.colores.add("rgba(192, 192, 192, 0.2)");
		this.colores.add("rgba(128, 128, 128, 0.2)");
		this.colores.add("rgba(128, 0, 0, 0.2)");
		this.colores.add("rgba(128, 128, 0, 0.2)");
		this.colores.add("rgba(128, 0, 128, 0.2)");
		this.colores.add("rgba(0, 128, 128, 0.2)");
		this.colores.add("rgba(0, 0, 128, 0.2)");  
		this.colores.add("rgba(139, 0, 0, 0.2)");
		this.colores.add("rgba(165, 42, 42, 0.2)");
		this.colores.add("rgba(178, 34, 34, 0.2)");
		this.colores.add("rgba(220, 20, 60, 0.2)");
		this.colores.add("rgba(255, 99, 71, 0.2)");
		this.colores.add("rgba(255, 127, 80, 0.2)");
		this.colores.add("rgba(205, 92, 92, 0.2)");
		this.colores.add("rgba(240, 128, 128, 0.2)");
		this.colores.add("rgba(240, 248, 255, 0.2)");
		this.colores.add("rgba(255, 250, 240, 0.2)");
		this.colores.add("rgba(230, 230, 250, 0.2)");
		this.colores.add("rgba(218, 112, 214, 0.2)");
		this.colores.add("rgba(250, 128, 114, 0.2)");
		this.colores.add("rgba(255, 160, 122, 0.2)");
		this.colores.add("rgba(255, 69, 0, 0.2)");
		this.colores.add("rgba(255, 140, 0, 0.2)");
		this.colores.add("rgba(255, 165, 0, 0.2)");
		this.colores.add("rgba(255, 215, 0, 0.2)");
		this.colores.add("rgba(184, 134, 11, 0.2)");
		this.colores.add("rgba(218, 165, 32, 0.2)");
		this.colores.add("rgba(238, 232, 170, 0.2)");
		this.colores.add("rgba(189, 183, 107, 0.2)");
		this.colores.add("rgba(240, 230, 140, 0.2)");
		this.colores.add("rgba(128, 128, 0, 0.2)");
		this.colores.add("rgba(255, 255, 0, 0.2)");
		this.colores.add("rgba(50, 205, 50, 0.2)");
		this.colores.add("rgba(144, 238, 144, 0.2)");
		this.colores.add("rgba(152, 251, 152, 0.2)");
		this.colores.add("rgba(175, 238, 238, 0.2)");
		this.colores.add("rgba(127, 255, 212, 0.2)");
		this.colores.add("rgba(154, 205, 50, 0.2)");
		this.colores.add("rgba(85, 107, 47, 0.2)");
		this.colores.add("rgba(107, 142, 35, 0.2)");
		this.colores.add("rgba(124, 252, 0, 0.2)"); 
		this.colores.add("rgba(0, 100, 0, 0.2)");
		this.colores.add("rgba(34, 139, 34, 0.2)");
		this.colores.add("rgba(70, 130, 180, 0.2)");
		this.colores.add("rgba(100, 149, 237, 0.2)");
		this.colores.add("rgba(0, 191, 255, 0.2)"); 
		this.colores.add("rgba(173, 216, 230, 0.2)");
		this.colores.add("rgba(255, 192, 203, 0.2)");
		this.colores.add("rgba(250, 235, 215, 0.2)");
		this.colores.add("rgba(135, 206, 250, 0.2)");
		this.colores.add("rgba(75, 0, 130, 0.2)");
		this.colores.add("rgba(255, 228, 196, 0.2)");
		this.colores.add("rgba(60, 179, 113, 0.2)");
		this.colores.add("rgba(32, 178, 170, 0.2)");
		this.colores.add("rgba(75, 0, 130, 0.2)");
		this.colores.add("rgba(245, 245, 220, 0.2)");
		this.colores.add("rgba(123, 104, 238, 0.2)");
		this.colores.add("rgba(139, 0, 139, 0.2)");
		this.colores.add("rgba(147, 112, 219, 0.2)");
		this.colores.add("rgba(199, 21, 133, 0.2)"); 
		this.colores.add("rgba(255, 250, 205, 0.2)");
		this.colores.add("rgba(160, 82, 45, 0.2)");
		this.colores.add("rgba(205, 133, 63, 0.2)");
		this.colores.add("rgba(244, 164, 96, 0.2)");
		this.colores.add("rgba(255, 105, 180, 0.2)");
		this.colores.add("rgba(148, 0, 211, 0.2)");
		this.colores.add("rgba(186, 85, 211, 0.2)");
		this.colores.add("rgba(216, 191, 216, 0.2)");
		this.colores.add("rgba(245, 255, 250, 0.2)");
		this.colores.add("rgba(210, 105, 30, 0.2)");
		this.colores.add("rgba(244, 164, 96, 0.2)");
		this.colores.add("rgba(188, 143, 143, 0.2)"); 
		this.colores.add("rgba(255, 218, 185, 0.2)");
		this.colores.add("rgba(176, 196, 222, 0.2)");
		this.colores.add("rgba(119, 136, 153, 0.2)");
		this.colores.add("rgba(255, 20, 147, 0.2)");
		this.colores.add("rgba(219, 112, 147, 0.2)");
		this.colores.add("rgba(255, 182, 193, 0.2)");
		this.colores.add("rgba(255, 192, 203, 0.2)");
		this.colores.add("rgba(106, 90, 205, 0.2)");
		this.colores.add("rgba(138, 43, 226, 0.2)");
		this.colores.add("rgba(152, 251, 152, 0.2)");
		this.colores.add("rgba(0,255,127, 0.2)");
		this.colores.add("rgba(47,79,79, 0.2)");
		this.colores.add("rgba(255, 239, 213, 0.2)");
	}

	/**
	 * Metodo que inicializar los bordes a usar.
	 */
	public void bordes() {
		this.bordes = new ArrayList<String>();
		this.bordes.add("rgba(255, 99, 132, 0.2)");
		this.bordes.add("rgba(255, 159, 64, 0.2)");
		this.bordes.add("rgba(255, 205, 86, 0.2)");
		this.bordes.add("rgba(75, 192, 192, 0.2)");
		this.bordes.add("rgba(54, 162, 235, 0.2)");
		this.bordes.add("rgba(153, 102, 255, 0.2)");
		this.bordes.add("rgba(201, 203, 207, 0.2)");
		this.bordes.add("rgba(199, 149, 149, 0.2)");  
		this.bordes.add("rgba(224, 52, 52, 0.2)");
		this.bordes.add("rgba(86, 67, 117, 0.2)");
		this.bordes.add("rgba(120, 252, 3, 0.2)");
		this.bordes.add("rgba(40, 200, 152, 0.2)");
		this.bordes.add("rgba(76, 134, 151, 0.2)");
		this.bordes.add("rgba(0, 128, 0, 0.2)");
		this.bordes.add("rgba(102, 205, 170, 0.2)");
		this.bordes.add("rgba(227, 119, 97, 0.2)");
		this.bordes.add("rgba(220, 220, 220, 0.2)");
		this.bordes.add("rgba(233, 150, 122, 0.2)");
		this.bordes.add("rgba(241, 7, 7, 0.2)");
		this.bordes.add("rgba(8, 253, 179, 0.2)");
		this.bordes.add("rgba(82, 229, 184, 0.2)");
		this.bordes.add("rgba(77, 225, 120, 0.2)");
		this.bordes.add("rgba(248, 61, 61, 0.2)");
		this.bordes.add("rgba(189, 159, 238, 0.2)");
		this.bordes.add("rgba(238, 130, 238, 0.2)");
		this.bordes.add("rgba(198, 37, 37, 0.2)");
		this.bordes.add("rgba(156, 7, 7, 0.2)");
		this.bordes.add("rgba(246, 122, 122, 0.2)");
		this.bordes.add("rgba(246, 168, 168, 0.2)");
		this.bordes.add("rgba(55, 4, 140, 0.2)");
		this.bordes.add("rgba(107, 65, 176, 0.2)");
		this.bordes.add("rgba(246, 48, 8, 0.2)");
		this.bordes.add("rgba(221, 45, 10, 0.2)");
		this.bordes.add("rgba(220, 66, 35, 0.2)");
		this.bordes.add("rgba(152, 135, 30, 0.2)");
		this.bordes.add("rgba(165, 37, 11, 0.2)");
		this.bordes.add("rgba(253, 92, 60, 0.2)");
		this.bordes.add("rgba(243, 226, 118, 0.2)");  
		this.bordes.add("rgba(97, 8, 243, 0.2)");
		this.bordes.add("rgba(62, 176, 141, 0.2)");
		this.bordes.add("rgba(161, 252, 224, 0.2)");
		this.bordes.add("rgba(180, 75, 113, 0.2)");
		this.bordes.add("rgba(206, 187, 67, 0.2)");
		this.bordes.add("rgba(16, 155, 112, 0.2)");
		this.bordes.add("rgba(160, 120, 225, 0.2)");
		this.bordes.add("rgba(167, 122, 240, 0.2)");
		this.bordes.add("rgba(255, 231, 76, 0.2)");
		this.bordes.add("rgba(251, 219, 15, 0.2)");
		this.bordes.add("rgba(95,158,160, 0.2)");
		this.bordes.add("rgba(147,112,219, 0.2)");
		this.bordes.add("rgba(153,50,204, 0.2)");
		this.bordes.add("rgba(240,230,140, 0.2)");
		this.bordes.add("rgba(203, 176, 6, 0.2)");
		this.bordes.add("rgba(155, 242, 78, 0.2)");
		this.bordes.add("rgba(138, 203, 81, 0.2)");
		this.bordes.add("rgba(187, 235, 144, 0.2)");
		this.bordes.add("rgba(110, 199, 172, 0.2)");
		this.bordes.add("rgba(193, 247, 230, 0.2)");   
		this.bordes.add("rgba(255, 2, 94, 0.2)");
		this.bordes.add("rgba(137, 9, 56, 0.2)");
		this.bordes.add("rgba(210, 49, 107, 0.2)");
		this.bordes.add("rgba(247, 99, 153, 0.2)");
		this.bordes.add("rgba(229, 110, 153, 0.2)");
		this.bordes.add("rgba(196, 138, 159, 0.2)");
		this.bordes.add("rgba(21, 118, 89, 0.2)");
		this.bordes.add("rgba(55, 4, 140, 0.2)");
		this.bordes.add("rgba(234, 197, 210, 0.2)");    
		this.bordes.add("rgba(135, 48, 234, 0.2)");
		this.bordes.add("rgba(172, 103, 249, 0.2)");
		this.bordes.add("rgba(161, 111, 218, 0.2)");
		this.bordes.add("rgba(93, 185, 11, 0.2)");  
		this.bordes.add("rgba(65, 129, 9, 0.2)");
		this.bordes.add("rgba(114, 194, 42, 0.2)");
		this.bordes.add("rgba(16, 138, 175, 0.2)");
		this.bordes.add("rgba(49, 161, 194, 0.2)");
		this.bordes.add("rgba(1, 197, 255, 0.2)");
		this.bordes.add("rgba(55, 4, 140, 0.2)");
		this.bordes.add("rgba(142, 226, 250, 0.2)");
		this.bordes.add("rgba(201, 158, 250, 0.2)");
		this.bordes.add("rgba(226, 203, 251, 0.2)");
		this.bordes.add("rgba(183, 174, 194, 0.2)"); 
		this.bordes.add("rgba(45, 108, 127, 0.2)");
		this.bordes.add("rgba(2, 104, 134, 0.2)");
		this.bordes.add("rgba(143, 213 234, 0.2)");
		this.bordes.add("rgba(129, 22, 250, 0.2)");
		this.bordes.add("rgba(85, 20, 158, 0.2)");
		this.bordes.add("rgba(116, 58, 183, 0.2)");
		this.bordes.add("rgba(165, 198, 208, 0.2)");
		this.bordes.add("rgba(155, 209, 225, 0.2)");   
		this.bordes.add("rgba(113, 73, 158, 0.2)");
		this.bordes.add("rgba(17, 240, 81, 0.2)");
		this.bordes.add("rgba(10, 206, 67, 0.2)");
		this.bordes.add("rgba(66, 255, 121, 0.2)");
		this.bordes.add("rgba(214, 37, 203, 0.2)");
		this.bordes.add("rgba(130, 62, 126, 0.2)");
		this.bordes.add("rgba(157, 112, 154, 0.2)");
		this.bordes.add("rgba(7, 138, 45, 0.2)");
		this.bordes.add("rgba(33, 100, 53, 0.2)");
		this.bordes.add("rgba(104, 238, 143, 0.2)");
		this.bordes.add("rgba(143, 244, 172, 0.2)");
		this.bordes.add("rgba(137, 187, 152, 0.2)");
		this.bordes.add("rgba(192, 234, 204, 0.2)"); 
		this.bordes.add("rgba(128, 25, 122, 0.2)");
		this.bordes.add("rgba(167, 15, 158, 0.2)");
		this.bordes.add("rgba(202, 33, 192, 0.2)");   
		this.bordes.add("rgba(255, 0, 0, 0.2)");
		this.bordes.add("rgba(0, 0, 255, 0.2)");
		this.bordes.add("rgba(60, 179, 113, 0.2)");
		this.bordes.add("rgba(238, 130, 238, 0.2)");
		this.bordes.add("rgba(255, 165, 0, 0.2)");
		this.bordes.add("rgba(106, 90, 205, 0.2)");
		this.bordes.add("rgba(0, 255, 0, 0.2)");
		this.bordes.add("rgba(255, 255, 0, 0.2)");
		this.bordes.add("rgba(0, 255, 255, 0.2)");
		this.bordes.add("rgba(255, 0, 255, 0.2)");
		this.bordes.add("rgba(192, 192, 192, 0.2)");
		this.bordes.add("rgba(128, 128, 128, 0.2)");
		this.bordes.add("rgba(128, 0, 0, 0.2)");
		this.bordes.add("rgba(128, 128, 0, 0.2)");
		this.bordes.add("rgba(128, 0, 128, 0.2)");
		this.bordes.add("rgba(0, 128, 128, 0.2)");
		this.bordes.add("rgba(0, 0, 128, 0.2)");  
		this.bordes.add("rgba(139, 0, 0, 0.2)");
		this.bordes.add("rgba(165, 42, 42, 0.2)");
		this.bordes.add("rgba(178, 34, 34, 0.2)");
		this.bordes.add("rgba(220, 20, 60, 0.2)");
		this.bordes.add("rgba(255, 99, 71, 0.2)");
		this.bordes.add("rgba(255, 127, 80, 0.2)");
		this.bordes.add("rgba(205, 92, 92, 0.2)");
		this.bordes.add("rgba(240, 128, 128, 0.2)");
		this.bordes.add("rgba(240, 248, 255, 0.2)");
		this.bordes.add("rgba(255, 250, 240, 0.2)");
		this.bordes.add("rgba(230, 230, 250, 0.2)");
		this.bordes.add("rgba(218, 112, 214, 0.2)");
		this.bordes.add("rgba(250, 128, 114, 0.2)");
		this.bordes.add("rgba(255, 160, 122, 0.2)");
		this.bordes.add("rgba(255, 69, 0, 0.2)");
		this.bordes.add("rgba(255, 140, 0, 0.2)");
		this.bordes.add("rgba(255, 165, 0, 0.2)");
		this.bordes.add("rgba(255, 215, 0, 0.2)");
		this.bordes.add("rgba(184, 134, 11, 0.2)");
		this.bordes.add("rgba(218, 165, 32, 0.2)");
		this.bordes.add("rgba(238, 232, 170, 0.2)");
		this.bordes.add("rgba(189, 183, 107, 0.2)");
		this.bordes.add("rgba(240, 230, 140, 0.2)");
		this.bordes.add("rgba(128, 128, 0, 0.2)");
		this.bordes.add("rgba(255, 255, 0, 0.2)");
		this.bordes.add("rgba(50, 205, 50, 0.2)");
		this.bordes.add("rgba(144, 238, 144, 0.2)");
		this.bordes.add("rgba(152, 251, 152, 0.2)");
		this.bordes.add("rgba(175, 238, 238, 0.2)");
		this.bordes.add("rgba(127, 255, 212, 0.2)");
		this.bordes.add("rgba(154, 205, 50, 0.2)");
		this.bordes.add("rgba(85, 107, 47, 0.2)");
		this.bordes.add("rgba(107, 142, 35, 0.2)");
		this.bordes.add("rgba(124, 252, 0, 0.2)"); 
		this.bordes.add("rgba(0, 100, 0, 0.2)");
		this.bordes.add("rgba(34, 139, 34, 0.2)");
		this.bordes.add("rgba(70, 130, 180, 0.2)");
		this.bordes.add("rgba(100, 149, 237, 0.2)");
		this.bordes.add("rgba(0, 191, 255, 0.2)"); 
		this.bordes.add("rgba(173, 216, 230, 0.2)");
		this.bordes.add("rgba(255, 192, 203, 0.2)");
		this.bordes.add("rgba(250, 235, 215, 0.2)");
		this.bordes.add("rgba(135, 206, 250, 0.2)");
		this.bordes.add("rgba(75, 0, 130, 0.2)");
		this.bordes.add("rgba(255, 228, 196, 0.2)");
		this.bordes.add("rgba(60, 179, 113, 0.2)");
		this.bordes.add("rgba(32, 178, 170, 0.2)");
		this.bordes.add("rgba(75, 0, 130, 0.2)");
		this.bordes.add("rgba(245, 245, 220, 0.2)");
		this.bordes.add("rgba(123, 104, 238, 0.2)");
		this.bordes.add("rgba(139, 0, 139, 0.2)");
		this.bordes.add("rgba(147, 112, 219, 0.2)");
		this.bordes.add("rgba(199, 21, 133, 0.2)"); 
		this.bordes.add("rgba(255, 250, 205, 0.2)");
		this.bordes.add("rgba(160, 82, 45, 0.2)");
		this.bordes.add("rgba(205, 133, 63, 0.2)");
		this.bordes.add("rgba(244, 164, 96, 0.2)");
		this.bordes.add("rgba(255, 105, 180, 0.2)");
		this.bordes.add("rgba(148, 0, 211, 0.2)");
		this.bordes.add("rgba(186, 85, 211, 0.2)");
		this.bordes.add("rgba(216, 191, 216, 0.2)");
		this.bordes.add("rgba(245, 255, 250, 0.2)");
		this.bordes.add("rgba(210, 105, 30, 0.2)");
		this.bordes.add("rgba(244, 164, 96, 0.2)");
		this.bordes.add("rgba(188, 143, 143, 0.2)"); 
		this.bordes.add("rgba(255, 218, 185, 0.2)");
		this.bordes.add("rgba(176, 196, 222, 0.2)");
		this.bordes.add("rgba(119, 136, 153, 0.2)");
		this.bordes.add("rgba(255, 20, 147, 0.2)");
		this.bordes.add("rgba(219, 112, 147, 0.2)");
		this.bordes.add("rgba(255, 182, 193, 0.2)");
		this.bordes.add("rgba(255, 192, 203, 0.2)");
		this.bordes.add("rgba(106, 90, 205, 0.2)");
		this.bordes.add("rgba(138, 43, 226, 0.2)");
		this.bordes.add("rgba(152, 251, 152, 0.2)");
		this.bordes.add("rgba(0,255,127, 0.2)");
		this.bordes.add("rgba(47,79,79, 0.2)");
		this.bordes.add("rgba(255, 239, 213, 0.2)");
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////
	public List<String> getColores() {
		return colores;
	}

	public void setColores(List<String> colores) {
		this.colores = colores;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<String> getBordes() {
		return bordes;
	}

	public void setBordes(List<String> bordes) {
		this.bordes = bordes;
	}
}
