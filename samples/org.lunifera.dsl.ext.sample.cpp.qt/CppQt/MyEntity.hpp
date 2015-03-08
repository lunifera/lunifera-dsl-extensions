
#ifndef MYENTITY_HPP_
#define MYENTITY_HPP_

#include <QObject>
#include <qvariant.h>

class MyEntity: public QObject {
Q_OBJECT

Q_PROPERTY(QString name READ name WRITE setName NOTIFY nameChanged FINAL)

Q_PROPERTY(int age READ age WRITE setAge NOTIFY ageChanged FINAL)


Q_PROPERTY(QVariantList others READ others NOTIFY othersChanged FINAL)


public:
	MyEntity(QObject *parent = 0);	
	MyEntity(QVariantMap myEntityMap);

	QVariantMap toMap();
	QVariantMap toForeignMap();

	QString name() const;
	void setName(QString name);

	int age() const;
	void setAge(int age);


	QVariantList others() const;


	virtual ~MyEntity();

Q_SIGNALS:

	void nameChanged(QString name);

	void ageChanged(int age);

	void othersChanged(MyOther others);


private:

	QVariantMap mMyEntityMap;
	
	void nameChanged(QString name);
	QString mName;
	void ageChanged(int age);
	int mAge;
	void othersChanged(MyOther others);
	MyOther mOthers;

	Q_DISABLE_COPY(MyEntity)
};
Q_DECLARE_METATYPE(MyEntity*)

#endif /* MYENTITY_HPP_ */

}
