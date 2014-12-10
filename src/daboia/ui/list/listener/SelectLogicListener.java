
package daboia.ui.list.listener;

import daboia.DaboiaLogic;
import daboia.domain.Player;
import daboia.plugin.PluginManager;
import daboia.ui.ComponentListChooser;
import daboia.ui.list.ComponentList;
import daboia.ui.list.LabelList;
import daboia.ui.list.PlayerList;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
        
public class SelectLogicListener extends MouseAdapter {
    
    private final Window parent;
    private final PlayerList list;
    private final Player player;
    
    public SelectLogicListener(Window parent, PlayerList list, Player player) {
        this.parent = parent;
        this.list = list;
        this.player = player;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        try {
            ComponentList componentList = new LabelList();
            componentList.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            componentList.addObjects(PluginManager.getLogicHandlers());
            
            ComponentListChooser chooser = new ComponentListChooser<>(parent, componentList);
            DaboiaLogic logicHandler = (DaboiaLogic) chooser.showDialog();
            
            if (logicHandler != null) {
                logicHandler = logicHandler.clone();
                player.setLogicHandler(logicHandler);
                list.refresh();
            }
    }

}