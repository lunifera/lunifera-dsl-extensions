
#ifndef MYOTHER_HPP_
#define MYOTHER_HPP_

#include <QObject>
#include <qvariant.h>

class MyOther: public QObject {
Q_OBJECT

Q_PROPERTY(MyEntity entity READ entity WRITE setEntity NOTIFY entityChanged FINAL)



public:
	MyEntity(QObject *parent = 0);	
	MyEntity(QVariantMap myOtherMap);

	QVariantMap toMap();
	QVariantMap toForeignMap();

	MyEntity entity() const;
	void setEntity(MyEntity entity);



	virtual ~MyOther();

Q_SIGNALS:

	void entityChanged(MyEntity entity);


private:

	QVariantMap mMyOtherMap;
	
	void entityChanged(MyEntity entity);
	MyEntity mEntity;

	Q_DISABLE_COPY(MyOther)
};
Q_DECLARE_METATYPE(MyOther*)

#endif /* MYOTHER_HPP_ */

}
