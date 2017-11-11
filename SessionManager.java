import java.util.ArrayList;

public class SessionManager implements ViewListener {

    private SessionList sessions = new SessionList();

    public SessionManager(){}

    @Override
    public synchronized void join(String name, ViewProxy proxy) {
        BnsModel model = sessions.getOpen();
        model.addBoardListener(proxy,name);
        proxy.setViewListener(model);
    }

    @Override
    public void newBoard() { }

    @Override
    public void ballClicked(int x) { }

    @Override
    public void stickClicked(int x) { }

    @Override
    public void quit() {

    }

    public class SessionList extends ArrayList<BnsModel>{

        public BnsModel getOpen(){
            for(BnsModel temp:this){
                if(temp.getNumberOfClients() < 2){
                    return temp;
                }
            }
            this.add(new BnsModel());
            return this.get(this.size()-1);
        }

    }

}