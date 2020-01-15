package com.metaconsultoria.root.scfilemanager;

public class MyComent {
        private long id;
        private String name;
        private String data_hr;
        private String coment;

        MyComent(){
        }

        MyComent(long id){
            this.id=id;
        }

        MyComent(String name,String text,String data_hr){
            this.name=name;
            this.data_hr=data_hr;
            this.coment=text;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getData_hr() {
            return data_hr;
        }

        public String getComent() {
            return coment;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setData_hr(String data_hr) {
            this.data_hr = data_hr;
        }

        public void setComent(String coment) {
            this.coment = coment;
        }
}
