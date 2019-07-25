/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s).  A complete listing of authors of this work is readily
 * available.  Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee.  No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used
 * in accordance with the terms of the license agreement entered into with
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.xhome.visitor;


import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.Identifiable;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.filter.NotPredicate;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.MajorLogMsg;


/**
 * Moves beans from one home to another.  This is useful when moving data from the journal to the database.  Data is
 * moved from the source to the temporary home to maintain a backup of migrated entries.
 *
 * The temporary home should be reliable with very low risk of error.  An XML home is a good candidate for persistent
 * backup or a Transient home is acceptable if persistent backups are not required.
 *
 * @since 8.2
 */
public class HomeMigrationVisitor implements Visitor {

    protected Home srcHome_;
    protected Home destHome_;
    protected Home tempHome_;
    protected Predicate updateExistingPredicate_;
    protected boolean removeFromSource_;

    public HomeMigrationVisitor(Home source, Home destination, boolean updateExisting) {
        this(source, destination, null, updateExisting);
    }

    public HomeMigrationVisitor(Home source, Home destination, Home temp, boolean updateExisting,
            boolean removeFromSource) {
        this(source, destination, temp, updateExisting ? True.instance() : new NotPredicate(True.instance()),
                removeFromSource);
    }

    public HomeMigrationVisitor(Home source, Home destination, Home temp, Predicate updateExistingPredicate,
            boolean removeFromSource) {
        destHome_ = destination;
        srcHome_ = source;
        tempHome_ = temp;
        updateExistingPredicate_ = updateExistingPredicate;
        removeFromSource_ = removeFromSource;
    }

    public HomeMigrationVisitor(Home source, Home destination, Home temp, boolean updateExisting) {
        this(source, destination, temp, updateExisting, true);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(Context ctx, Object obj) throws AgentException, AbortVisitException {
        try {
            if (tempHome_ != null) {
                Object alreadyBackedUp = tempHome_.find(ctx, obj);
                if (alreadyBackedUp == null) {
                    tempHome_.create(ctx, obj);
                } else {
                    tempHome_.store(ctx, obj);
                }

                if (removeFromSource_) {
                    srcHome_.remove(ctx, obj);
                }
            }

            Object existingEntry = destHome_.find(((Identifiable) obj).ID());
            if (existingEntry != null) {
                if (updateExistingPredicate_.f(ctx, existingEntry)) {
                    new InfoLogMsg(this,
                            "Overwriting " + obj.getClass().getName() + " with ID " + ((Identifiable) obj).ID()
                                    + " in the destination data store.  Existing entry: " + existingEntry, null)
                            .log(ctx);
                    destHome_.store(ctx, ((AbstractBean) obj).deepClone());
                } else {
                    new InfoLogMsg(this,
                            "Skipping migration of " + obj.getClass().getName() + " with ID " + ((Identifiable) obj)
                                    .ID()
                                    + " because an entry with that ID already exists in the destination data store.",
                            null).log(ctx);
                }
            } else if (obj instanceof AbstractBean) {
                destHome_.create(ctx, ((AbstractBean) obj).deepClone());
            } else {
                destHome_.create(ctx, obj);
            }

            if (tempHome_ != null) {
                if (existingEntry == null || updateExistingPredicate_.f(ctx, existingEntry)) {
                    tempHome_.remove(ctx, obj);
                }
            } else {
                srcHome_.remove(ctx, obj);
            }
        } catch (HomeException e) {
            if (obj instanceof Identifiable) {
                new MajorLogMsg(this,
                        "Error moving " + obj.getClass().getSimpleName() + " " + ((Identifiable) obj).ID() + " from "
                                + srcHome_ + " to " + destHome_, e).log(ctx);
            } else {
                new MajorLogMsg(this, "Error moving bean " + obj + " from " + srcHome_ + " to " + destHome_, e)
                        .log(ctx);
            }
        } catch (CloneNotSupportedException e) {
            throw new AbortVisitException(e);
        }
    }
}
