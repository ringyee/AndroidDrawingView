package com.vilyever.androiddrawingview;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vilyever.drawingview.VDDrawingView;
import com.vilyever.drawingview.brush.VDCenterCircleBrush;
import com.vilyever.drawingview.brush.VDCircleBrush;
import com.vilyever.drawingview.brush.VDDrawingBrush;
import com.vilyever.drawingview.brush.VDEllipseBrush;
import com.vilyever.drawingview.brush.VDIsoscelesTriangleBrush;
import com.vilyever.drawingview.brush.VDLineBrush;
import com.vilyever.drawingview.brush.VDPenBrush;
import com.vilyever.drawingview.brush.VDPolygonBrush;
import com.vilyever.drawingview.brush.VDRectangleBrush;
import com.vilyever.drawingview.brush.VDRhombusBrush;
import com.vilyever.drawingview.brush.VDRightAngledTriangleBrush;
import com.vilyever.drawingview.brush.VDRoundedRetangleBrush;
import com.vilyever.drawingview.brush.VDShapeBrush;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * DrawingFragment
 * AndroidDrawingBoard <com.vilyever.androiddrawingboard>
 * Created by vilyever on 2015/9/21.
 * Feature:
 */
public class DrawingFragment extends Fragment {
    private final DrawingFragment self = this;

    private VDDrawingView drawingView;

    private Button undoButton;
    private Button redoButton;
    private Button clearButton;

    private Button penButton;
    private Button eraserButton;
    private Button shapeButton;
    private Button backgroundColorButton;

    private Button thicknessButton;
    private Button colorButton;
    private Button solidColorButton;
    private Button oneStrokeOneLayerButton;

    private ThicknessAdjustController thicknessAdjustController;

    private List<Button> singleSelectionButtons;

    private List<VDShapeBrush> shapeBrushes = new ArrayList<>();
    private VDPenBrush penBrush;
    private VDPenBrush eraserBrush;

    /* #Constructors */
    public DrawingFragment() {
        // Required empty public constructor
    }


    /* #Overrides */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.drawing_fragment, container, false);

        self.drawingView = (VDDrawingView) rootView.findViewById(R.id.drawingView);
        self.drawingView.setDelegate(new VDDrawingView.DrawingDelegate() {
            @Override
            public void undoStateDidChangeFromDrawingView(VDDrawingView drawingView, boolean canUndo, boolean canRedo) {
                self.undoButton.setEnabled(canUndo);
                self.redoButton.setEnabled(canRedo);
            }
        });

        self.undoButton = (Button) rootView.findViewById(R.id.undoButton);
        self.undoButton.setEnabled(false);
        self.undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.drawingView.undo();
            }
        });

        self.redoButton = (Button) rootView.findViewById(R.id.redoButton);
        self.redoButton.setEnabled(false);
        self.redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.drawingView.redo();
            }
        });

        self.clearButton = (Button) rootView.findViewById(R.id.clearButton);
        self.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.drawingView.clear();
            }
        });

        self.penBrush = VDPenBrush.defaultBrush();
        self.drawingView.setDrawingBrush(self.penBrush);
        self.penButton = (Button) rootView.findViewById(R.id.penButton);
        self.penButton.setSelected(true);
        self.penButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.selectButton(self.singleSelectionButtons, self.penButton);
                self.drawingView.setDrawingBrush(self.penBrush);
            }
        });

        self.eraserBrush = VDPenBrush.defaultBrush().setIsEraser(true);
        self.eraserButton = (Button) rootView.findViewById(R.id.eraserButton);
        self.eraserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.selectButton(self.singleSelectionButtons, self.eraserButton);
                self.drawingView.setDrawingBrush(self.eraserBrush);
            }
        });

        self.shapeBrushes.add(VDPolygonBrush.defaultBrush());
        self.shapeBrushes.add(VDLineBrush.defaultBrush());
        self.shapeBrushes.add(VDRectangleBrush.defaultBrush());
        self.shapeBrushes.add(VDRoundedRetangleBrush.defaultBrush());
        self.shapeBrushes.add(VDCircleBrush.defaultBrush());
        self.shapeBrushes.add(VDEllipseBrush.defaultBrush());
        self.shapeBrushes.add(VDRightAngledTriangleBrush.defaultBrush());
        self.shapeBrushes.add(VDIsoscelesTriangleBrush.defaultBrush());
        self.shapeBrushes.add(VDRhombusBrush.defaultBrush());
        self.shapeBrushes.add(VDCenterCircleBrush.defaultBrush());
        self.shapeButton = (Button) rootView.findViewById(R.id.shapeButton);
        self.shapeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VDDrawingBrush brush = null;

                if (!v.isSelected()) {
                    if (v.getTag() == null) {
                        v.setTag(1);
                    }
                    self.selectButton(self.singleSelectionButtons, (Button) v);
                }
                else {
                    int index = (int) v.getTag() + 1;
                    index = index % self.shapeBrushes.size();
                    v.setTag(index);
                }

                brush = self.shapeBrushes.get((Integer) v.getTag());
                self.drawingView.setDrawingBrush(brush);
                ((Button) v).setText(brush.getClass().getSimpleName());
            }
        });

        self.backgroundColorButton = (Button) rootView.findViewById(R.id.backgroundColorButton);
        self.backgroundColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                self.drawingView.setBackgroundColor(Color.argb(255, Math.abs(random.nextInt()) % 256, Math.abs(random.nextInt()) % 256, Math.abs(random.nextInt()) % 256), 0);
            }
        });

        self.thicknessButton = (Button) rootView.findViewById(R.id.thicknessButton);
        self.thicknessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.getThicknessAdjustController().setThickness((int) self.drawingView.getDrawingBrush().getSize());
                self.getThicknessAdjustController().popupFromView(v);
            }
        });

        self.colorButton = (Button) rootView.findViewById(R.id.colorButton);
        self.colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int color = Color.argb(Math.abs(random.nextInt()) % 256, Math.abs(random.nextInt()) % 256, Math.abs(random.nextInt()) % 256, Math.abs(random.nextInt()) % 256);
                v.setBackgroundColor(color);

                self.penBrush.setColor(color);
                for (VDDrawingBrush brush : self.shapeBrushes) {
                    brush.setColor(color);
                }
            }
        });

        self.solidColorButton = (Button) rootView.findViewById(R.id.solidColorButton);
        self.solidColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int color = Color.argb(Math.abs(random.nextInt()) % 256, Math.abs(random.nextInt()) % 256, Math.abs(random.nextInt()) % 256, Math.abs(random.nextInt()) % 256);
                if (self.drawingView.getDrawingBrush() instanceof VDShapeBrush) {
                    v.setBackgroundColor(color);

                    for (VDShapeBrush brush : self.shapeBrushes) {
                        brush.setSolidColor(color);
                    }
                }
            }
        });

        self.oneStrokeOneLayerButton = (Button) rootView.findViewById(R.id.oneStrokeOneLayerButton);
        self.oneStrokeOneLayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                self.penBrush.setOneStrokeToLayer(true);
                for (VDDrawingBrush brush : self.shapeBrushes) {
                    brush.setOneStrokeToLayer(true);
                }
            }
        });

        self.singleSelectionButtons = new ArrayList<>();
        self.singleSelectionButtons.add(self.penButton);
        self.singleSelectionButtons.add(self.eraserButton);
        self.singleSelectionButtons.add(self.shapeButton);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    
    /* #Accessors */

    public ThicknessAdjustController getThicknessAdjustController() {
        if (self.thicknessAdjustController == null) {
            self.thicknessAdjustController = new ThicknessAdjustController(self.getActivity());
            self.thicknessAdjustController.setDelegate(new ThicknessAdjustController.ThicknessDelegate() {
                @Override
                public void thicknessDidChangeFromThicknessAdjustController(ThicknessAdjustController controller, int thickness) {
                    self.penBrush.setSize(thickness);
                    self.eraserBrush.setSize(thickness);
                    for (VDDrawingBrush brush : self.shapeBrushes) {
                        brush.setSize(thickness);
                    }
                }
            });
        }
        return thicknessAdjustController;
    }
    /* #UI Actions */
    
    /* #Delegates */     
     
    /* #Private Methods */
    private void selectButton(List<Button> buttons, Button button) {
        for (Button b : buttons) {
            b.setSelected(b == button);
        }
    }
    
    /* #Public Methods */

    /* #Classes */

    /* #Interfaces */     
     
    /* #Annotations @interface */    
    
    /* #Enums */
}